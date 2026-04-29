# Project Roadmap & Enterprise Enhancements 🚀

This document outlines the strategic technical improvements required to elevate this integration to a "Top-Tier Enterprise" level. These points address reliability, architecture, and security gaps identified during the initial code review.

## 1. Reliability & Fault Tolerance

* **Refactor SID Injection:** Move away from manual header injection in `GusSearchService`. Implement a `ClientInterceptor` or use `WebServiceMessageCallback` within the `marshalSendAndReceive` method. This ensures thread safety and decouples business logic from transport implementation.
* **Smart Cache Eviction:** Current session caching (55m) is optimistic. Implement an AOP-based or Retry-linked mechanism that detects "Invalid Session" errors from GUS and triggers an immediate `@CacheEvict` to force a re-login.
* **Advanced Fallback Strategies:** Move beyond simple error logging in Fallbacks. Implement "Graceful Degradation" by returning cached stale data or partial DTOs to keep the calling system operational during GUS downtime.

## 2. Security

* **Zero-Secrets Policy:** Remove the default API key from `application.yaml`. Transition to a strictly environment-driven configuration (e.g., `${GUS_API_KEY}`) or integrate with a Secret Manager (HashiCorp Vault, AWS Secrets Manager) for production environments.
* **Secure Transport:** Ensure that all production WSDL URLs and endpoints use HTTPS to prevent man-in-the-middle attacks on sensitive business data.

## 3. Architecture & Clean Code

* **Logic Decoupling:** Extract XML/CDATA parsing logic from `GusSearchService` into a dedicated `GusResponseParser`. The service should only coordinate the flow, not handle low-level string manipulations or `XmlMapper` initialization.
* **Domain-Driven Validation:** Replace generic Regex patterns in Controllers with custom JSR-303 annotations like `@ValidNip` and `@ValidRegon`. This improves reusability and keeps controllers clean.
* **Dependency Injection:** Ensure `XmlMapper` and other utility objects are managed beans (Singletons) injected via constructor, rather than being instantiated inside service methods.

## 4. Observability & Tracing

* **Micrometer Tracing Integration:** While the manual `CorrelationIdFilter` works, migrating to Spring Observability (Micrometer Tracing) is the enterprise standard. It automatically handles trace propagation across HTTP, SOAP, and async boundaries.
* **Health Indicators:** Add a custom `HealthIndicator` for the GUS service to the `/actuator/health` endpoint. This allows monitoring tools to detect if the integration is "UP" based on session validity and connectivity.

## 5. Testing Standards

* **Contract Testing:** Implement Spring Cloud Contract to ensure that changes in the SOAP/REST mapping don't break downstream consumers.
* **Integration Testing:** Use `@SpringBootTest` with WireMock to simulate various GUS failure scenarios (timeouts, 500 errors, malformed XML) to verify Circuit Breaker behavior.