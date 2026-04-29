package com.piotrekcieslak.gusintegrationspring.integration;

import com.piotrcieslak.gus.wsdl.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import java.util.Optional;

@Service
public class GusAuthService {
    private final WebServiceTemplate webServiceTemplate;
    @Value("${gus.api.key}")
    private String apiKey;

    public GusAuthService(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    @Cacheable("gus-session")
    public String getSessionToken() {
        ObjectFactory factory = new ObjectFactory();
        Zaloguj request = factory.createZaloguj();
        request.setPKluczUzytkownika(factory.createZalogujPKluczUzytkownika(apiKey));

        ZalogujResponse response = (ZalogujResponse) webServiceTemplate.marshalSendAndReceive(
            request,
            msg -> ((SoapMessage) msg).setSoapAction("\"http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Zaloguj\"")
        );

        return Optional.ofNullable(response.getZalogujResult())
                .map(jakarta.xml.bind.JAXBElement::getValue)
                .orElseThrow(() -> new RuntimeException("GUS: Logowanie nieudane."));
    }
}