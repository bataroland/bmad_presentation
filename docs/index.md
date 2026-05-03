# Library API — BMAD Migration Demo

> 🎤 **[View the presentation →](./presentation/)**
>
> A 60-minute developer talk demonstrating the BMAD methodology
> through a Java 8 / Spring Boot 1.5 → Java 25 / Spring Boot 3.x migration.

---

## Project Documentation Index

> Generated: 2026-04-06 | Mode: Initial Scan | Scan Level: Quick
> _(BMAD-generated project scan — see [README](https://github.com/bataroland/bmad_presentation) for the full story)_

## Project Overview

- **Type:** Monolith (single cohesive codebase)
- **Primary Language:** Java 1.8
- **Framework:** Spring Boot 1.5.22.RELEASE
- **Architecture:** Layered (Controller → Service → Repository → Entity)
- **Packaging:** WAR (Maven)

## Quick Reference

- **Tech Stack:** Java 8, Spring Boot 1.5, Spring Data JPA, Hibernate 5, H2, Springfox Swagger 2
- **Entry Point:** `LibraryApplication.java` (extends SpringBootServletInitializer)
- **Architecture Pattern:** 3-tier layered architecture
- **API Endpoints:** 12 REST endpoints across 2 controllers
- **Entities:** 3 (Book, Member, Loan) with 2 relationships
- **Run:** `mvn spring-boot:run` → http://localhost:8081/library

## Generated Documentation

- [Project Overview](./project-overview.md)
- [Architecture](./architecture.md)
- [Source Tree Analysis](./source-tree-analysis.md)
- [API Contracts](./api-contracts.md)
- [Data Models](./data-models.md)
- [Development Guide](./development-guide.md)

## Getting Started

1. Open project in devcontainer (VS Code → Reopen in Container)
2. Run: `mvn spring-boot:run`
3. Browse API: http://localhost:8081/library/swagger-ui.html
4. Explore DB: http://localhost:8081/library/h2-console (JDBC URL: `jdbc:h2:mem:librarydb`, user: `sa`)
