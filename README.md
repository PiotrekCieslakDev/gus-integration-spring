# GUS Integration Spring 🏢

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-blue.svg)](https://www.oracle.com/java/)
[![Resilience4j](https://img.shields.io/badge/Resilience4j-Circuit%20Breaker-orange.svg)](https://resilience4j.readme.io/)

A high-performance, enterprise-grade integration gateway (SOAP to REST) for the Polish Central Statistical Office (GUS) BIR 1.1 service (REGON Internet Database). This project provides a streamlined REST API to retrieve official business entity data using NIP or REGON identifiers[.

Built with a focus on **reliability**, **scalability**, and **modern Java features**, it handles the complexities of GUS SOAP communication — including session management, CDATA parsing, and WS-Addressing — transparently for the end user.

## 🌟 Key Features

* **RESTful Interface:** Modern JSON API wrapping legacy GUS SOAP services.
* **Smart Session Management:** Automated login and session ID (`sid`) handling with **Caffeine Cache** to minimize overhead.
* **Enterprise Resilience:** Implementation of **Circuit Breaker** and **Retry** patterns via Resilience4j to handle external service instability.
* **Advanced Data Parsing:** Custom logic to extract and deserialize nested XML data from GUS's `CDATA` responses.
* **Structured Observability:** JSON-formatted logging with **Correlation ID** propagation for distributed tracing.
* **Automated Documentation:** Full OpenAPI 3.0 specification with an interactive Swagger UI.

## 🛠️ Tech Stack

* **Runtime:** Java 25
* **Framework:** Spring Boot 3.5.14
* **Communication:** Spring Web Services & JAXB (SOAP), Jackson (XML/JSON)
* **Resilience:** Resilience4j (Spring Cloud Circuit Breaker)
* **Caching:** Caffeine Cache
* **Validation:** Spring Boot Starter Validation
* **Logging:** Logback with Logstash Encoder
* **Documentation:** Springdoc OpenAPI

## 🚀 Getting Started

### Prerequisites
* **JDK 25** or higher.
* **Maven 3.9+**.
* Valid GUS BIR 1.1 API Key (Test or Production).

### Configuration
The application expects an API key. For security, it is recommended to provide this via environment variables rather than hardcoding in application.yaml.

```bash
Set your key using: export GUS_API_KEY="your_secret_api_key"
```

### Installation & Execution
1. **Clone the repository:**
   git clone https://github.com/yourusername/gus-integration-spring.git
2. **Build the project:** (Includes JAXB class generation from WSDL)
   mvn clean install
3. **Run the application:**
   mvn spring-boot:run

## 📚 API Reference

Once the service is running, navigate to: http://localhost:8080/swagger-ui.html.

### Primary Endpoints
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | /api/nip/{nip} | Retrieve basic entity data by NIP (10 digits) |
| GET | /api/nip/{nip}/full | Retrieve detailed legal report by NIP |
| GET | /api/regon/{regon} | Retrieve basic entity data by REGON (9/14 digits) |
| GET | /api/regon/{regon}/full | Retrieve detailed report by REGON |

## 🏗️ Architecture Highlights

* **Fault Tolerance:** The GusSearchService is protected by @CircuitBreaker, ensuring that if the GUS service becomes unresponsive, the application stays healthy and provides graceful fallbacks.
* **Automated SOAP Headers:** Custom transport logic ensures the sid (Session ID) and WS-Addressing headers are correctly injected into SOAP requests.
* **Clean Code:** Heavy use of Lombok to reduce boilerplate and Global Exception Handling for standardized API error responses.
* **Structured Logging:** Every request is tagged with a correlationId, allowing for easy log aggregation and debugging in distributed environments.

## 📄 License
This project is licensed under the MIT License.