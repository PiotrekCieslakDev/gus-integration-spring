package com.piotrekcieslak.gusintegrationspring.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "root")
public class FullCompanyWrapper {
    @JacksonXmlProperty(localName = "dane")
    private FullCompanyDto data;
}