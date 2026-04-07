# Architecture — Library API

> Generated: 2026-04-06 | Scan Level: Quick

## Executive Summary

Library API is a simple book lending management system built as a traditional Java 8 web application. It follows a classic layered architecture pattern (Controller → Service → Repository → Entity) using Spring Boot 1.5.x, deployed as a WAR file to Apache Tomcat.

## Technology Stack

| Category | Technology | Version | Notes |
|----------|-----------|---------|-------|
| Language | Java | 1.8 (Temurin 8.0.482) | LTS, EOL for public updates |
| Framework | Spring Boot | 1.5.22.RELEASE | EOL since August 2019 |
| Web | Spring MVC | 4.3.x (via Boot 1.5) | Embedded in spring-boot-starter-web |
| ORM | Hibernate | 5.0.12.Final | Via Spring Data JPA |
| Database | H2 | (managed by Boot) | In-memory, development only |
| Validation | Hibernate Validator | (managed by Boot) | javax.validation (Bean Validation 1.1) |
| API Docs | Springfox Swagger | 2.9.2 | Swagger UI at /swagger-ui.html |
| Build | Maven | 3.8.7 | WAR packaging |
| App Server | Apache Tomcat | 8.5.43 (embedded) / 9.x (external) | WAR deployment |
| Test | JUnit | 4.x | Via spring-boot-starter-test |
| Test | Mockito | 1.x | Via spring-boot-starter-test |

## Architecture Pattern

**Layered Architecture (3-tier)**

```
┌─────────────────────────────────┐
│         REST Controllers        │  ← HTTP request handling, validation
│   BookController, LoanController│
├─────────────────────────────────┤
│         Service Layer           │  ← Business logic, rules
│   BookService, LoanService      │
├─────────────────────────────────┤
│       Repository Layer          │  ← Data access (Spring Data JPA)
│   BookRepo, MemberRepo, LoanRepo│
├─────────────────────────────────┤
│         JPA Entities            │  ← Domain model (Book, Member, Loan)
├─────────────────────────────────┤
│       H2 Database (in-memory)   │  ← Persistence
└─────────────────────────────────┘
```

## Data Architecture

- **3 entities:** Book, Member, Loan
- **2 relationships:** Loan → Book (ManyToOne), Loan → Member (ManyToOne)
- **Database:** H2 in-memory (auto-DDL via Hibernate)
- **Seed data:** `data.sql` loaded on startup
- See [Data Models](./data-models.md) for details

## API Design

- **12 REST endpoints** across 2 controllers
- **Context path:** `/library`
- **No authentication** — open API
- **Error handling:** Manual HashMap-based responses
- See [API Contracts](./api-contracts.md) for full endpoint list

## Deployment Model

- **Packaging:** WAR (via maven-war-plugin 3.2.3)
- **Embedded mode:** `mvn spring-boot:run` on port 8081
- **External Tomcat:** Deploy `library-api.war`
- **WAR support:** `LibraryApplication` extends `SpringBootServletInitializer`
- **WAR descriptor:** Traditional `web.xml` (Servlet 3.1)
- **Dev environment:** Podman-based devcontainer with Java 8 + Tomcat 9

## Testing Strategy

- **Framework:** JUnit 4 + Mockito
- **Test files:** 2 test classes
  - `BookServiceTest.java` — unit tests for service layer
  - `BookControllerTest.java` — controller tests
- **No integration tests** detected
- **No test configuration** (uses defaults)

## Notable Technical Debt / Migration Concerns

| Area | Current State | Migration Target |
|------|--------------|-----------------|
| Java version | 1.8 | 25 LTS |
| Spring Boot | 1.5.22 (EOL) | 3.x+ |
| Namespace | javax.* | jakarta.* |
| Date/Time API | java.util.Date, Calendar, SimpleDateFormat | java.time.* |
| Code idioms | Anonymous classes, manual for-loops | Lambdas, Streams |
| Repository API | findOne() | findById().orElse() |
| Config property | server.context-path | server.servlet.context-path |
| Test framework | JUnit 4 | JUnit 5 (Jupiter) |
| API docs | Springfox 2.x | SpringDoc OpenAPI 3 |
| Deployment | WAR/Tomcat | Paketo buildpack (containerized) |
