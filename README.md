# Analytics Service

**Analytics Service** is a dedicated microservice within our unified application ecosystem. It gathers and processes events from various parts of the system to provide comprehensive analytics data across all domains. The service supports flexible querying by any entity (using its ID), event type, and time interval, enabling data-driven insights throughout the application.

---

## Overview

- **Unified Analytics Platform:**  
  Collects analytics events from all microservices (such as user_service, post_service, account_service, payment_service, etc.) and aggregates data by entity ID (receiverId), event type, and time interval.

- **Flexible Querying:**  
  The REST endpoint (e.g., `/analytics`) accepts:
  - An **entity ID** to target the specific data.
  - An **event type** as a string (or number) that is converted to an enum (`EventType`), with case-insensitive matching.
  - An optional **interval** parameter (string or number) that maps to an `Interval` enum.
  - Optional start and end date-time parameters (as strings to be parsed into `LocalDateTime`) for custom time ranges.  
  **Note:** The user must provide either an interval parameter or both date parameters.

- **Validation and Error Handling:**  
  All input parameters are rigorously validated and converted to their corresponding types. Any validation errors result in proper HTTP error responses, ensuring that clients receive clear feedback.

- **Integration in the Ecosystem:**  
  As part of the larger application, Analytics Service plays a crucial role in monitoring system behavior and performance across all microservices.

---

## Service Template

This service is built as part of a standardized Spring Boot project template.

### Technologies Used

- [Spring Boot](https://spring.io/projects/spring-boot) – Main framework
- [PostgreSQL](https://www.postgresql.org/) – Primary relational database
- [Redis](https://redis.io/) – Used as a cache and for message queuing via pub/sub
- [Testcontainers](https://testcontainers.com/) – For isolated testing with a real database
- [Liquibase](https://www.liquibase.org/) – For managing database schema migrations
- [Gradle](https://gradle.org/) – Build system
- [Lombok](https://projectlombok.org/) – For simplified POJO class handling
- [MapStruct](https://mapstruct.org/) – For efficient mapping between POJOs

### Database

- The PostgreSQL database is managed in a separate service ([infra](../infra)).
- Redis is also deployed as a single instance in the [infra](../infra) service.
- Liquibase automatically applies the necessary migrations to a bare PostgreSQL instance when the application starts.
- Integration tests use Testcontainers to run an isolated instance of PostgreSQL.
- Both JdbcTemplate and JPA (Hibernate) are demonstrated in the codebase.
