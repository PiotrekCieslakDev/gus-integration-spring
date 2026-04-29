package com.piotrekcieslak.gusintegrationspring.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import java.util.List;

@Data
@JacksonXmlRootElement(localName = "root")
public class CompanyResponse {
    @JacksonXmlProperty(localName = "dane")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CompanyDto> companies;
}