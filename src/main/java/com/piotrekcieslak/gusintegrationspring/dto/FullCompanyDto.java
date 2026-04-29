package com.piotrekcieslak.gusintegrationspring.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Pełny raport o podmiocie z bazy REGON (BIR 1.1)")
public class FullCompanyDto {

    // --- Identifying Data (Common for both types) ---
    @Schema(description = "Numer NIP (Tax Identification Number)")
    private String nip;

    @Schema(description = "Numer REGON (National Business Registry Number)")
    private String regon;

    @Schema(description = "Nazwa podmiotu")
    private String name;

    @Schema(description = "Typ podmiotu (P - prawna, F - fizyczna)")
    private String type;

    @JacksonXmlProperty(localName = "praw_statusNip")
    @Schema(description = "GUS: praw_statusNip")
    private String nipStatus;

    // --- Sole Trader Specific Data | Dane dla OSOBY PRAWNEJ (praw_) ---
    @JacksonXmlProperty(localName = "praw_nazwaSkrocona")
    @Schema(description = "GUS: praw_nazwaSkrocona")
    private String shortName;

    @JacksonXmlProperty(localName = "praw_numerWRejestrzeEwidencji")
    @Schema(description = "GUS: praw_numerWRejestrzeEwidencji")
    private String krsNumber;

    @JacksonXmlProperty(localName = "praw_dataWpisuDoRejestruEwidencji")
    @Schema(description = "GUS: praw_dataWpisuDoRejestruEwidencji")
    private String registrationEntryDate;

    @JacksonXmlProperty(localName = "praw_dataPowstania")
    @Schema(description = "GUS: praw_dataPowstania")
    private String creationDate;

    @JacksonXmlProperty(localName = "praw_dataRozpoczeciaDzialalnosci")
    @Schema(description = "GUS: praw_dataRozpoczeciaDzialalnosci")
    private String startDate;

    @JacksonXmlProperty(localName = "praw_dataWpisuDoRegon")
    @Schema(description = "GUS: praw_dataWpisuDoRegon")
    private String regonEntryDate;

    @JacksonXmlProperty(localName = "praw_dataZawieszeniaDzialalnosci")
    @Schema(description = "GUS: praw_dataZawieszeniaDzialalnosci")
    private String suspensionDate;

    @JacksonXmlProperty(localName = "praw_dataWznowieniaDzialalnosci")
    @Schema(description = "GUS: praw_dataWznowieniaDzialalnosci")
    private String resumptionDate;

    @JacksonXmlProperty(localName = "praw_dataZaistnieniaZmiany")
    @Schema(description = "GUS: praw_dataZaistnieniaZmiany")
    private String lastChangeDate;

    @JacksonXmlProperty(localName = "praw_dataZakonczeniaDzialalnosci")
    @Schema(description = "GUS: praw_dataZakonczeniaDzialalnosci")
    private String endDate;

    @JacksonXmlProperty(localName = "praw_adSiedzKraj_Nazwa")
    @Schema(description = "GUS: praw_adSiedzKraj_Nazwa")
    private String country;

    @JacksonXmlProperty(localName = "praw_adSiedzWojewodztwo_Nazwa")
    @Schema(description = "GUS: praw_adSiedzWojewodztwo_Nazwa")
    private String voivodeship;

    @JacksonXmlProperty(localName = "praw_adSiedzPowiat_Nazwa")
    @Schema(description = "GUS: praw_adSiedzPowiat_Nazwa")
    private String county;

    @JacksonXmlProperty(localName = "praw_adSiedzGmina_Nazwa")
    @Schema(description = "GUS: praw_adSiedzGmina_Nazwa")
    private String community;

    @JacksonXmlProperty(localName = "praw_adSiedzKodPocztowy")
    @Schema(description = "GUS: praw_adSiedzKodPocztowy")
    private String zipCode;

    @JacksonXmlProperty(localName = "praw_adSiedzMiejscowosc_Nazwa")
    @Schema(description = "GUS: praw_adSiedzMiejscowosc_Nazwa")
    private String city;

    @JacksonXmlProperty(localName = "praw_adSiedzUlica_Nazwa")
    @Schema(description = "GUS: praw_adSiedzUlica_Nazwa")
    private String street;

    @JacksonXmlProperty(localName = "praw_adSiedzNumerNieruchomosci")
    @Schema(description = "GUS: praw_adSiedzNumerNieruchomosci")
    private String houseNumber;

    @JacksonXmlProperty(localName = "praw_adSiedzNumerLokalu")
    @Schema(description = "GUS: praw_adSiedzNumerLokalu")
    private String apartmentNumber;

    @JacksonXmlProperty(localName = "praw_numerTelefonu")
    @Schema(description = "GUS: praw_numerTelefonu")
    private String phone;

    @JacksonXmlProperty(localName = "praw_adresEmail")
    @Schema(description = "GUS: praw_adresEmail")
    private String email;

    @JacksonXmlProperty(localName = "praw_adresStronyinternetowej")
    @Schema(description = "GUS: praw_adresStronyinternetowej")
    private String website;

    @JacksonXmlProperty(localName = "praw_podstawowaFormaPrawna_Nazwa")
    @Schema(description = "GUS: praw_podstawowaFormaPrawna_Nazwa")
    private String basicLegalForm;

    @JacksonXmlProperty(localName = "praw_szczegolnaFormaPrawna_Nazwa")
    @Schema(description = "GUS: praw_szczegolnaFormaPrawna_Nazwa")
    private String specificLegalForm;

    @JacksonXmlProperty(localName = "praw_formaFinansowania_Nazwa")
    @Schema(description = "GUS: praw_formaFinansowania_Nazwa")
    private String financingForm;

    @JacksonXmlProperty(localName = "praw_formaWlasnosci_Nazwa")
    @Schema(description = "GUS: praw_formaWlasnosci_Nazwa")
    private String ownershipForm;

    @JacksonXmlProperty(localName = "praw_organRejestrowy_Nazwa")
    @Schema(description = "GUS: praw_organRejestrowy_Nazwa")
    private String registryAuthority;

    @JacksonXmlProperty(localName = "praw_rodzajRejestruEwidencji_Nazwa")
    @Schema(description = "GUS: praw_rodzajRejestruEwidencji_Nazwa")
    private String registryType;

    @JacksonXmlProperty(localName = "praw_liczbaJednLokalnych")
    @Schema(description = "GUS: praw_liczbaJednLokalnych")
    private Integer localUnitsCount;

    // --- Data for Corporate Entity | Dane dla OSOBY FIZYCZNEJ (fiz_) ---
    @JacksonXmlProperty(localName = "fiz_regon9")
    @Schema(description = "GUS: fiz_regon9")
    private String physicalRegon;

    @JacksonXmlProperty(localName = "fiz_nip")
    @Schema(description = "GUS: fiz_nip")
    private String physicalNip;

    @JacksonXmlProperty(localName = "fiz_statusNip")
    @Schema(description = "GUS: fiz_statusNip")
    private String physicalNipStatus;

    @JacksonXmlProperty(localName = "fiz_imie1")
    @Schema(description = "GUS: fiz_imie1")
    private String firstName;

    @JacksonXmlProperty(localName = "fiz_imie2")
    @Schema(description = "GUS: fiz_imie2")
    private String secondName;

    @JacksonXmlProperty(localName = "fiz_nazwisko")
    @Schema(description = "GUS: fiz_nazwisko")
    private String lastName;

    @JacksonXmlProperty(localName = "fiz_dataWpisuPodmiotuDoRegon")
    @Schema(description = "GUS: fiz_dataWpisuPodmiotuDoRegon")
    private String physicalRegonEntryDate;

    @JacksonXmlProperty(localName = "fiz_dataZaistnieniaZmiany")
    @Schema(description = "GUS: fiz_dataZaistnieniaZmiany")
    private String physicalLastChangeDate;

    @JacksonXmlProperty(localName = "fiz_dataSkresleniaPodmiotuZRegon")
    @Schema(description = "GUS: fiz_dataSkresleniaPodmiotuZRegon")
    private String physicalEndDate;

    @JacksonXmlProperty(localName = "fiz_podstawowaFormaPrawna_Nazwa")
    @Schema(description = "GUS: fiz_podstawowaFormaPrawna_Nazwa")
    private String physicalBasicLegalForm;

    @JacksonXmlProperty(localName = "fiz_szczegolnaFormaPrawna_Nazwa")
    @Schema(description = "GUS: fiz_szczegolnaFormaPrawna_Nazwa")
    private String physicalSpecificLegalForm;

    @JacksonXmlProperty(localName = "fiz_formaFinansowania_Nazwa")
    @Schema(description = "GUS: fiz_formaFinansowania_Nazwa")
    private String physicalFinancingForm;

    @JacksonXmlProperty(localName = "fiz_formaWlasnosci_Nazwa")
    @Schema(description = "GUS: fiz_formaWlasnosci_Nazwa")
    private String physicalOwnershipForm;

    @JacksonXmlProperty(localName = "fiz_dzialalnoscCeidg")
    @Schema(description = "GUS: fiz_dzialalnoscCeidg")
    private String isCeidg;

    @JacksonXmlProperty(localName = "fiz_dzialalnoscRolnicza")
    @Schema(description = "GUS: fiz_dzialalnoscRolnicza")
    private String isAgricultural;

    @JacksonXmlProperty(localName = "fiz_dzialalnoscPozostala")
    @Schema(description = "GUS: fiz_dzialalnoscPozostala")
    private String isOtherActivity;

    @JacksonXmlProperty(localName = "fiz_dzialalnoscSkreslonaDo20141108")
    @Schema(description = "GUS: fiz_dzialalnoscSkreslonaDo20141108")
    private String isDeletedBefore2014;

    @JacksonXmlProperty(localName = "fiz_liczbaJednLokalnych")
    @Schema(description = "GUS: fiz_liczbaJednLokalnych")
    private Integer physicalLocalUnitsCount;

    @JacksonXmlProperty(localName = "fiz_adSiedzUlica_Nazwa")
    @Schema(description = "GUS: fiz_adSiedzUlica_Nazwa")
    private String physicalStreet;
}