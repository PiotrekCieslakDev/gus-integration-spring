# GUS Integration Spring 🏢

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-blue.svg)](https://www.oracle.com/java/)
[![Resilience4j](https://img.shields.io/badge/Resilience4j-Circuit%20Breaker-orange.svg)](https://resilience4j.readme.io/)

Nowoczesna bramka integracyjna (SOAP -> REST) dla usługi GUS BIR 1.1 (Baza Internetowa REGON). Projekt dostarcza wygodne i ustandaryzowane API RESTowe do pobierania podstawowych oraz szczegółowych danych podmiotów gospodarczych na podstawie NIP oraz REGON.

Aplikacja została zaprojektowana z myślą o standardach **Enterprise**, oferując ustrukturyzowane logowanie, wzorce niezawodnościowe (Resilience) oraz wbudowane buforowanie sesji.

## 🌟 Główne funkcjonalności

*   **Wyszukiwanie po NIP:** Pobieranie podstawowych danych oraz pełnych raportów dla osób prawnych.
*   **Wyszukiwanie po REGON:** Pobieranie danych dla osób fizycznych i prawnych.
*   **Automatyzacja Sesji (GUS SID):** Samodzielne zarządzanie cyklem życia sesji i automatyczne logowanie przy użyciu klucza API, z optymalnym buforowaniem tokenów (Caffeine Cache).
*   **Niezawodność (Resilience4j):** Wbudowane mechanizmy *Retry* (ponawianie żądań) oraz *Circuit Breaker* (bezpiecznik) chroniące system przed awariami zewnętrznego API GUS.
*   **Ustrukturyzowane logowanie (JSON):** Przygotowane pod stos ELK/Splunk logowanie z propagacją identyfikatorów korelacji (`correlationId`), ułatwiające trace'owanie zapytań.
*   **Dokumentacja OpenAPI (Swagger):** Zautomatyzowana dokumentacja kontraktów REST i generowanie interfejsu testowego.

## 🛠️ Stos technologiczny

*   **Język:** Java 25
*   **Framework:** Spring Boot 3.5.14
*   **Web Services:** Spring Web Services (SOAP), JAXB
*   **Resilience:** Spring Cloud Circuitbreaker (Resilience4j)
*   **Cache:** Caffeine
*   **Dokumentacja:** Springdoc OpenAPI (Swagger UI)
*   **Logowanie:** Logback z Logstash Encoder
*   **Narzędzia:** Lombok, Maven

## 🚀 Uruchomienie projektu

### Wymagania wstępne
*   Zainstalowane JDK 25
*   Maven 3.8+
*   Klucz API do środowiska testowego/produkcyjnego GUS BIR 1.1

### Konfiguracja
Aby uniknąć twardego kodowania sekretów w repozytorium, klucz API GUS należy przekazać jako zmienną środowiskową.

W pliku `application.yaml` lub za pomocą zmiennych środowiskowych ustaw niezbędne właściwości:

```bash
export GUS_API_KEY="twój_klucz_api_tutaj"