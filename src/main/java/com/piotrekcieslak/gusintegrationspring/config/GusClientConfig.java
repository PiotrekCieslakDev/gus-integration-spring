package com.piotrekcieslak.gusintegrationspring.config;

import com.piotrekcieslak.gusintegrationspring.integration.GusHeaderInterceptor;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.SimpleHttpComponents5MessageSender;

@Configuration
public class GusClientConfig {

    @Value("${gus.api.url}")
    private String gusUrl;

    @Bean
    public SaajSoapMessageFactory messageFactory() {
        SaajSoapMessageFactory factory = new SaajSoapMessageFactory();
        factory.setSoapVersion(SoapVersion.SOAP_12);
        return factory;
    }

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.piotrcieslak.gus.wsdl");
        marshaller.setMtomEnabled(true);
        return marshaller;
    }

    // 1. Definiujemy Managera Puli Połączeń
    @Bean
    public PoolingHttpClientConnectionManager connectionManager() {
        PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
        // Maksymalna liczba wszystkich połączeń w aplikacji
        poolingConnManager.setMaxTotal(50);
        // Maksymalna liczba połączeń do JEDNEGO hosta (w tym wypadku domeny GUS)
        poolingConnManager.setDefaultMaxPerRoute(20);
        return poolingConnManager;
    }

    // 2. Budujemy głównego klienta HTTP używając naszej puli i timeoutów
    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager connectionManager) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofSeconds(3)) // Czas oczekiwania na wydanie połączenia z puli
                .setResponseTimeout(Timeout.ofSeconds(10))         // Maksymalny czas oczekiwania na dane z GUS
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                // Utrzymanie otwartych połączeń (keep-alive) przez 30 sekund
                .setKeepAliveStrategy((response, context) -> TimeValue.ofSeconds(30))
                .build();
    }

    // 3. Adapter łączący Spring WS z Apache HttpClient 5 (Nowa klasa zapobiegająca deprecjacji)
    @Bean
    public SimpleHttpComponents5MessageSender messageSender(CloseableHttpClient httpClient) {
        return new SimpleHttpComponents5MessageSender(httpClient);
    }

    // 4. Wstrzykujemy nowy messageSender do template'a
    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller,
                                                 SaajSoapMessageFactory messageFactory,
                                                 GusHeaderInterceptor headerInterceptor,
                                                 SimpleHttpComponents5MessageSender messageSender) {
        WebServiceTemplate template = new WebServiceTemplate(messageFactory);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setDefaultUri(gusUrl);
        template.setInterceptors(new ClientInterceptor[]{headerInterceptor});

        template.setMessageSender(messageSender);

        return template;
    }
}