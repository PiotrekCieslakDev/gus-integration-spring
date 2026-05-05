# Project Roadmap & Enterprise Enhancements

Ten dokument opisuje strategiczne kroki i ulepszenia techniczne, które wprowadzają integrację z API GUS na poziom **Top-Tier Enterprise (FAANG Quality)**. Dokument jest na bieżąco aktualizowany wraz z postępami prac.

---

## ✅ Zrealizowane Kamienie Milowe (Faza 1 i 2)

Następujące krytyczne usprawnienia wydajnościowe i niezawodnościowe zostały już wdrożone:

*   **Ekstremalna Wydajność I/O (Project Loom):** Aplikacja natywnie wykorzystuje Wirtualne Wątki (Java 25), co drastycznie zwiększa przepustowość przy operacjach blokujących (I/O) bez zużywania pamięci RAM.
*   **Pula Połączeń (Connection Pooling):** Wdrożono `Apache HttpClient 5`. Zamiast otwierać nowe gniazdo TCP dla każdego żądania, aplikacja utrzymuje otwarte (Keep-Alive) i gotowe do użycia połączenia, eliminując narzut na handshake SSL.
*   **Bezpieczne Timeouty:** Skonfigurowano rygorystyczne limity czasu połączenia (3s) oraz odczytu (10s), chroniąc aplikację przed zablokowaniem wątków w przypadku awarii po stronie GUS.
*   **Inteligentne Kolejkowanie (Bucket4j):** Całkowicie wyeliminowano ryzyko zablokowania przez GUS (Throttling). `GusRateLimiter` precyzyjnie egzekwuje trzy kaskadowe limity (3/sek, 100/min, 6000/h), a nadmiarowe zapytania są bezkosztowo "usypiane" w pamięci aplikacji.
*   **Odporność na Awarie (Resilience4j):** Mechanizmy `@Retry` oraz `@CircuitBreaker` chronią warstwę biznesową przed propagacją błędów z niestabilnych usług zewnętrznych.

---

## 🚀 Do wdrożenia: Faza 3 - Architektura i Bezpieczeństwo

Kolejne kroki mają na celu odchudzenie logiki biznesowej i zabezpieczenie danych.

*   **Automatyzacja Mapowania (MapStruct):** Należy zastąpić ręczne przepisywanie właściwości (z `CompanyDto` do `FullCompanyDto`) zautomatyzowanym mapperem generowanym w czasie kompilacji. Zapobiegnie to błędom ludzkim i przyspieszy dodawanie nowych pól.
*   **Transport Error Interceptor:** Logika parsowania surowego XML w poszukiwaniu tagów `<ErrorCode>` musi zostać usunięta z `GusSearchService`. Zamiast tego, należy wdrożyć `ClientInterceptor` w warstwie Spring WS, który przechwyci błąd transportowy i rzuci biznesowy wyjątek zanim dane trafią do serwisu.
*   **Maskowanie Danych Wrażliwych (PII):** Ponieważ aplikacja eksportuje logi w formacie JSON (`LogstashEncoder`), należy skonfigurować reguły maskujące w `logback-spring.xml` (np. wygwiazdkowanie numerów NIP i REGON `9542******`), aby spełnić wymogi RODO i bezpieczeństwa Enterprise.

---

## 🚀 Do wdrożenia: Faza 4 - Skalowalność (Cloud-Native) i Observability

Przygotowanie aplikacji do działania w klastrze (np. Kubernetes) i monitorowania na produkcji.

*   **Rozproszona Sesja (Redis + Docker Compose):**
    *   Obecny `Caffeine Cache` działa tylko w pamięci pojedynczej instancji. Należy zmigrować zarządzanie sesją SID do **Redisa** (`spring-boot-starter-data-redis`).
    *   Pozwoli to na bezproblemowe skalowanie horyzontalne (wiele instancji aplikacji korzystających z jednego logowania do GUS).
    *   Wymaga stworzenia `Dockerfile` (Multi-stage build dla Javy 25) oraz `docker-compose.yml` do lokalnej orkiestracji usług (App + Redis).
*   **Metryki Biznesowe (Prometheus & Actuator):** Dodanie eksportera Prometheus. Umożliwi to monitorowanie w Grafanie kluczowych wskaźników:
    *   Liczby zapytań czekających w kolejce Bucket4j.
    *   Czasu odpowiedzi z GUS-u.
    *   Stanów Circuit Breakera (Closed/Open/Half-Open).

---

## 🔮 Przyszłość: Faza 5 - Quality Assurance

*   **Contract Testing:** Wdrożenie Spring Cloud Contract dla zagwarantowania, że zmiany w mapowaniach DTO nie zepsują integracji z klientami frontendowymi/mikroserwisami.
*   **Zaawansowane Testy Integracyjne (WireMock):** Symulowanie zrywania połączeń, opóźnień (np. 15-sekundowych) i błędów HTTP 500 z GUS-u, aby udowodnić niezawodność mechanizmów Bucket4j i Circuit Breaker w środowisku testowym.