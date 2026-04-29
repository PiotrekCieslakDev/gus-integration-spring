package com.piotrekcieslak.gusintegrationspring.config;

import com.piotrekcieslak.gusintegrationspring.integration.GusHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;

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
        //Hardcoded for a reason :)
        marshaller.setContextPath("com.piotrcieslak.gus.wsdl");
        marshaller.setMtomEnabled(true);
        return marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller, 
                                                 SaajSoapMessageFactory messageFactory,
                                                 GusHeaderInterceptor headerInterceptor) {
        WebServiceTemplate template = new WebServiceTemplate(messageFactory);
        template.setMarshaller(marshaller);
        template.setUnmarshaller(marshaller);
        template.setDefaultUri(gusUrl);
        template.setInterceptors(new ClientInterceptor[]{headerInterceptor});
        return template;
    }
}