package com.piotrekcieslak.gusintegrationspring.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import javax.xml.namespace.QName;

@Component
public class GusHeaderInterceptor implements ClientInterceptor {
    @Value("${gus.api.url}")
    private String gusUrl;
    private static final String WSA_NS = "http://www.w3.org/2005/08/addressing";

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        SoapMessage soapMessage = (SoapMessage) messageContext.getRequest();
        SoapHeader header = soapMessage.getSoapHeader();
        
        // IMPORTANT! Addressee (To)
        QName toName = new QName(WSA_NS, "To", "wsa");
        header.addHeaderElement(toName).setText(gusUrl);

        // Sync Action with HTTP
        String action = soapMessage.getSoapAction();
        if (action != null && !action.isEmpty()) {
            action = action.replace("\"", "");
            QName actionName = new QName(WSA_NS, "Action", "wsa");
            header.addHeaderElement(actionName).setText(action);
        }
        return true;
    }
    @Override public boolean handleResponse(MessageContext mc) { return true; }
    @Override public boolean handleFault(MessageContext mc) { return true; }
    @Override public void afterCompletion(MessageContext mc, Exception ex) { }
}