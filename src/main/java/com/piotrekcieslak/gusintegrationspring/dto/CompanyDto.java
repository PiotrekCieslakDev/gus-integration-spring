package com.piotrekcieslak.gusintegrationspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Podstawowe dane podmiotu zwracane przez wyszukiwarkę (DaneSzukajPodmioty)")
public class CompanyDto {

    @JacksonXmlProperty(localName = "Nip")
    @Schema(description = "GUS: Nip")
    private String nip;

    @JacksonXmlProperty(localName = "Regon")
    @Schema(description = "GUS: Regon")
    private String regon;

    @JacksonXmlProperty(localName = "Nazwa")
    @Schema(description = "GUS: Nazwa")
    private String name;

    @JacksonXmlProperty(localName = "Wojewodztwo")
    @Schema(description = "GUS: Wojewodztwo")
    private String voivodeship;

    @JacksonXmlProperty(localName = "Powiat")
    @Schema(description = "GUS: Powiat")
    private String county;

    @JacksonXmlProperty(localName = "Gmina")
    @Schema(description = "GUS: Gmina")
    private String district;

    @JacksonXmlProperty(localName = "Miejscowosc")
    @Schema(description = "GUS: Miejscowosc")
    private String city;

    @JacksonXmlProperty(localName = "KodPocztowy")
    @Schema(description = "GUS: KodPocztowy")
    private String zipCode;

    @JacksonXmlProperty(localName = "Ulica")
    @Schema(description = "GUS: Ulica")
    private String street;

    @JacksonXmlProperty(localName = "NrNieruchomosci")
    @Schema(description = "GUS: NrNieruchomosci")
    private String houseNumber;

    @JacksonXmlProperty(localName = "NrLokalu")
    @Schema(description = "GUS: NrLokalu")
    private String apartmentNumber;

    @JacksonXmlProperty(localName = "Typ")
    @Schema(description = "GUS: Typ (P - osoba prawna, F - osoba fizyczna)")
    private String type; // P - prawna, F - fizyczna
}