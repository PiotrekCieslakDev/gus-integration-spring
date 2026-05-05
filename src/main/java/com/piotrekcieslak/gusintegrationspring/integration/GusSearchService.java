package com.piotrekcieslak.gusintegrationspring.integration;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.piotrcieslak.gus.wsdl.*;
import com.piotrekcieslak.gusintegrationspring.config.GusRateLimiter;
import com.piotrekcieslak.gusintegrationspring.dto.CompanyDto;
import com.piotrekcieslak.gusintegrationspring.dto.CompanyResponse;
import com.piotrekcieslak.gusintegrationspring.dto.FullCompanyDto;
import com.piotrekcieslak.gusintegrationspring.dto.FullCompanyWrapper;
import com.piotrekcieslak.gusintegrationspring.dto.GusErrorResponse;
import com.piotrekcieslak.gusintegrationspring.exception.GusException;
import com.piotrekcieslak.gusintegrationspring.exception.GusNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

/**
 * Serwis integracyjny z API GUS BIR 1.1.
 * Implementuje wzorce Resilience (Retry, Circuit Breaker) oraz ustrukturyzowane logowanie.
 */
@Slf4j
@Service
public class GusSearchService {

    private final WebServiceTemplate webServiceTemplate;
    private final GusAuthService authService;
    private final GusRateLimiter rateLimiter;

    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectFactory factory = new ObjectFactory();

    public GusSearchService(WebServiceTemplate webServiceTemplate,
                            GusAuthService authService,
                            GusRateLimiter gusRateLimiter) {
        this.webServiceTemplate = webServiceTemplate;
        this.authService = authService;
        this.rateLimiter = gusRateLimiter;
    }

    // --- NIP Searching ---

    @Retry(name = "gusApi")
    @CircuitBreaker(name = "gusApi")
    public CompanyDto szukajPoNip(String nip) {
        log.info("Rozpoczęcie wyszukiwania podstawowego dla NIP: {}", nip);
        ParametryWyszukiwania p = factory.createParametryWyszukiwania();
        p.setNip(factory.createParametryWyszukiwaniaNip(nip));
        return wykonajWyszukiwanie(p);
    }

    // --- REGON Searching ---

    @Retry(name = "gusApi")
    @CircuitBreaker(name = "gusApi")
    public CompanyDto szukajPoRegon(String regon) {
        log.info("Rozpoczęcie wyszukiwania podstawowego dla REGON: {}", regon);
        ParametryWyszukiwania p = factory.createParametryWyszukiwania();
        p.setRegon(factory.createParametryWyszukiwaniaRegon(regon));
        return wykonajWyszukiwanie(p);
    }

    // --- Full responses ---

    @Retry(name = "gusApi")
    @CircuitBreaker(name = "gusApi")
    public FullCompanyDto pobierzPelnyRaport(String nip) {
        CompanyDto basic = szukajPoNip(nip);
        return pobierzRaportSzczegolowy(basic);
    }

    @Retry(name = "gusApi")
    @CircuitBreaker(name = "gusApi")
    public FullCompanyDto pobierzPelnyRaportPoRegon(String regon) {
        CompanyDto basic = szukajPoRegon(regon);
        return pobierzRaportSzczegolowy(basic);
    }

    // --- Private helpers - Consider dividing according to SRP ---

    private CompanyDto wykonajWyszukiwanie(ParametryWyszukiwania p) {
        DaneSzukajPodmioty request = factory.createDaneSzukajPodmioty();
        request.setPParametryWyszukiwania(factory.createDaneSzukajPodmiotyPParametryWyszukiwania(p));

        rateLimiter.waitForToken();

        DaneSzukajPodmiotyResponse response = (DaneSzukajPodmiotyResponse) webServiceTemplate.marshalSendAndReceive(
                request, msg -> prepareSoapHeaders(msg, "DaneSzukajPodmioty"));

        String rawXml = response.getDaneSzukajPodmiotyResult().getValue();
        
        // Check if response contains error structure
        if (isErrorResponse(rawXml)) {
            handleErrorResponse(rawXml);
        }
        
        CompanyResponse companyResponse = deserialize(rawXml, CompanyResponse.class);

        if (companyResponse.getCompanies() == null || companyResponse.getCompanies().isEmpty()) {
            log.warn("Nie znaleziono podmiotu w bazie GUS.");
            throw new GusNotFoundException("Brak danych w rejestrze REGON dla podanego identyfikatora.");
        }

        return companyResponse.getCompanies().getFirst();
    }

    private FullCompanyDto pobierzRaportSzczegolowy(CompanyDto basic) {
        // Wybór raportu na podstawie typu podmiotu: P (Prawna) lub F (Fizyczna)
        // Pick report type according to company type: P (prawna, Corporate Entity) or F (fizyczna, Sole trader)
        String reportName = "P".equalsIgnoreCase(basic.getType()) ? "BIR11OsPrawna" : "BIR11OsFizycznaDaneOgolne";
        log.debug("Pobieranie raportu szczegółowego: {} dla REGON: {}", reportName, basic.getRegon());

        DanePobierzPelnyRaport request = factory.createDanePobierzPelnyRaport();
        request.setPRegon(factory.createDanePobierzPelnyRaportPRegon(basic.getRegon()));
        request.setPNazwaRaportu(factory.createDanePobierzPelnyRaportPNazwaRaportu(reportName));

        rateLimiter.waitForToken();

        DanePobierzPelnyRaportResponse response = (DanePobierzPelnyRaportResponse) webServiceTemplate.marshalSendAndReceive(
                request, msg -> prepareSoapHeaders(msg, "DanePobierzPelnyRaport"));

        String fullXml = response.getDanePobierzPelnyRaportResult().getValue();
        
        // Check if response contains error structure
        if (isErrorResponse(fullXml)) {
            handleErrorResponse(fullXml);
        }
        
        FullCompanyDto fullData = deserialize(fullXml, FullCompanyWrapper.class).getData();

        // Mapping first request response to the final full dto model
        fullData.setNip(basic.getNip());
        fullData.setRegon(basic.getRegon());
        fullData.setName(basic.getName());
        fullData.setType(basic.getType());

        return fullData;
    }

    // --- Fallbacks (Resilience) ---

    public CompanyDto fallbackBasicSearch(String identifier, Throwable t) {
        log.error("Awaria połączenia z GUS (Basic Search). Ident: {}, Błąd: {}", identifier, t.getMessage());
        throw new GusException("Usługa wyszukiwania GUS jest chwilowo niedostępna. Spróbuj ponownie później.");
    }
    
    public CompanyDto fallbackBasicSearchNotFound(String identifier, GusNotFoundException t) {
        // Just re-throw the exception without triggering retry
        throw t;
    }

    public FullCompanyDto fallbackFullReport(String identifier, Throwable t) {
        log.error("Awaria połączenia z GUS (Full Report). Ident: {}, Błąd: {}", identifier, t.getMessage());
        throw new GusException("Nie można pobrać pełnych danych z GUS. System wejdzie w tryb awaryjny.");
    }
    
    public FullCompanyDto fallbackFullReportNotFound(String identifier, GusNotFoundException t) {
        // Just re-throw the exception without triggering retry
        throw t;
    }

    // --- Helpers ---

    private void prepareSoapHeaders(org.springframework.ws.WebServiceMessage message, String method) {
        SoapMessage soapMsg = (SoapMessage) message;
        soapMsg.setSoapAction("\"http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/" + method + "\"");

        String sid = authService.getSessionToken();
        HttpUrlConnection conn = (HttpUrlConnection) TransportContextHolder.getTransportContext().getConnection();
        conn.getConnection().setRequestProperty("sid", sid);
    }

    private <T> T deserialize(String xml, Class<T> clazz) {
        try {
            return xmlMapper.readValue(xml, clazz);
        } catch (Exception e) {
            log.error("Błąd deserializacji XML do klasy {}: {}", clazz.getSimpleName(), e.getMessage());
            throw new GusException("Błąd podczas przetwarzania danych otrzymanych z GUS.");
        }
    }
    
    private boolean isErrorResponse(String xml) {
        return xml.contains("<ErrorCode>") || xml.contains("ErrorCode");
    }
    
    private void handleErrorResponse(String xml) {
        try {
            GusErrorResponse errorResponse = deserialize(xml, GusErrorResponse.class);
            if (errorResponse.getDane() != null) {
                GusErrorResponse.ErrorData errorData = errorResponse.getDane();
                String errorCode = errorData.getErrorCode();
                String errorMessage = errorData.getErrorMessagePl();
                
                log.warn("Otrzymano błąd z GUS. Kod: {}, Wiadomość: {}", errorCode, errorMessage);
                
                // Handle specific error codes
                if ("4".equals(errorCode)) {
                    throw new GusNotFoundException(errorMessage != null ? errorMessage : "Nie znaleziono podmiotu dla podanych kryteriów wyszukiwania.");
                } else {
                    throw new GusException(errorMessage != null ? errorMessage : "Błąd podczas komunikacji z usługą GUS.");
                }
            }
        } catch (Exception e) {
            if (e instanceof GusNotFoundException || e instanceof GusException) {
                throw e;
            }
            log.error("Błąd podczas przetwarzania odpowiedzi błędu GUS: {}", e.getMessage());
            throw new GusException("Nieoczekiwany błąd podczas komunikacji z usługą GUS.");
        }
    }
}