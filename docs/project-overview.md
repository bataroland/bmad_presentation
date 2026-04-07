# Project Overview — Library API

> Generated: 2026-04-06 | Scan Level: Quick

## Summary

**Library API** is a simple book lending management REST API built with Java 8 and Spring Boot 1.5.x. It provides CRUD operations for books and a loan management system for library members. The application is packaged as a WAR file for deployment to Apache Tomcat.

## Purpose

Demonstration project for showcasing BMAD (Business-Manager-Architect-Developer) methodology on a brownfield Java migration scenario: migrating from Java 8 / Spring Boot 1.5 to Java 25 LTS / Spring Boot 3.x.

## Tech Stack Summary

| Component | Technology |
|-----------|-----------|
| Language | Java 1.8 (Temurin) |
| Framework | Spring Boot 1.5.22.RELEASE |
| Database | H2 (in-memory) |
| ORM | Hibernate 5.0.x / Spring Data JPA |
| API Docs | Springfox Swagger 2.9.2 |
| Build | Maven 3.8.7 (WAR) |
| Server | Tomcat 8.5 (embedded) / 9.x (external) |
| Tests | JUnit 4 + Mockito |

## Architecture

- **Type:** Monolith
- **Pattern:** Layered (Controller → Service → Repository → Entity)
- **API Endpoints:** 12 REST endpoints
- **Entities:** 3 (Book, Member, Loan)

## Repository Structure

- **Type:** Monolith (single cohesive codebase)
- **Language:** Java
- **Source files:** 14 (12 main + 2 test)

## Key Links

- [Architecture](./architecture.md)
- [API Contracts](./api-contracts.md)
- [Data Models](./data-models.md)
- [Source Tree Analysis](./source-tree-analysis.md)
- [Development Guide](./development-guide.md)
