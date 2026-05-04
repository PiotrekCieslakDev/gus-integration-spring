package com.piotrekcieslak.gusintegrationspring.controller;

import com.piotrekcieslak.gusintegrationspring.dto.CompanyDto;
import com.piotrekcieslak.gusintegrationspring.dto.FullCompanyDto;
import com.piotrekcieslak.gusintegrationspring.integration.GusSearchService;
import com.piotrekcieslak.gusintegrationspring.validation.nip.ValidNip;
import com.piotrekcieslak.gusintegrationspring.validation.regon.ValidRegon;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "GUS API", description = "Integracja z BIR 1.1/1.2 - Wyszukiwanie po NIP i REGON")
@Validated
public class CompanyController {

    private final GusSearchService searchService;

    public CompanyController(GusSearchService searchService) {
        this.searchService = searchService;
    }

    @Operation(summary = "Podstawowe dane po NIP")
    @GetMapping("/nip/{nip}")
    public ResponseEntity<CompanyDto> getBasicByNip(
            @Parameter(description = "NIP osoby prawnej (np. Europejska sp. z o.o.)", example = "9542300619")
            @ValidNip
            @PathVariable String nip) {
        return ResponseEntity.ok(searchService.szukajPoNip(nip));
    }

    @Operation(summary = "Pełny raport po NIP")
    @GetMapping("/nip/{nip}/full")
    public ResponseEntity<FullCompanyDto> getFullByNip(
            @Parameter(description = "NIP osoby prawnej (np. Europejska sp. z o.o.)", example = "9542300619")
            @ValidNip
            @PathVariable String nip) {
        return ResponseEntity.ok(searchService.pobierzPelnyRaport(nip));
    }

    @Operation(summary = "Podstawowe dane po REGON")
    @GetMapping("/regon/{regon}")
    public ResponseEntity<CompanyDto> getBasicByRegon(
            @Parameter(description = "REGON osoby fizycznej (np. Marek XXXXXXXX)", example = "356352625")
            @ValidRegon
            @PathVariable String regon) {
        return ResponseEntity.ok(searchService.szukajPoRegon(regon));
    }

    @Operation(summary = "Pełny raport po REGON",
            description = "Pozwala pobrać pełne dane osób fizycznych w sandboxie (gdzie NIP jest ukryty)")
    @GetMapping("/regon/{regon}/full")
    public ResponseEntity<FullCompanyDto> getFullByRegon(
            @Parameter(description = "REGON osoby fizycznej (np. Marek XXXXXXXX)", example = "356352625")
            @ValidRegon
            @PathVariable String regon) {
        return ResponseEntity.ok(searchService.pobierzPelnyRaportPoRegon(regon));
    }
}