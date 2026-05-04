package com.piotrekcieslak.gusintegrationspring.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "root")
public class GusErrorResponse {
    
    @JacksonXmlProperty(localName = "dane")
    private ErrorData dane;
    
    @Data
    public static class ErrorData {
        @JacksonXmlProperty(localName = "ErrorCode")
        private String errorCode;
        
        @JacksonXmlProperty(localName = "ErrorMessagePl")
        private String errorMessagePl;
        
        @JacksonXmlProperty(localName = "ErrorMessageEn")
        private String errorMessageEn;
        
        @JacksonXmlProperty(localName = "Nip")
        private String nip;
        
        @JacksonXmlProperty(localName = "Regon")
        private String regon;
        
        @JacksonXmlProperty(localName = "Krs")
        private String krs;
    }
}
