---
title: "Product Brief Distillate: Library API Migration"
type: llm-distillate
source: "product-brief-library-api-migration.md"
created: "2026-04-06"
purpose: "Token-efficient context for downstream PRD creation"
---

# Product Brief Distillate: Library API Migration

## Migration Strategy Decisions

- **Phased migration chosen:** Spring Boot 1.5 → 2.7 → 3.x (not direct jump). Spring's own team recommends this path; Spring Security 5.8 was specifically released as an intermediate stepping stone. Each phase should be a separate epic with clear milestones and a green build gate before proceeding.
- **Target runtime:** Java 25 LTS (GA September 2025, stable by April 2026). Spring Boot 3.x has certified Java 25 support.
- **Deployment model shift:** WAR/Tomcat → executable JAR → Paketo buildpack OCI container. Removes web.xml, SpringBootServletInitializer boilerplate.
- **GraalVM native-image explicitly deferred** to Phase 2 — adds significant build complexity, not all libraries fully support it, and it's not needed for the demo's goals.

## Critical Migration Landmines

- **javax.crypto trap (CRITICAL):** `javax.persistence.*`, `javax.validation.*` etc. must migrate to `jakarta.*`. But JDK-owned packages (`javax.crypto`, `javax.net.ssl`, `javax.security.*`) must NEVER be renamed. Naive find-and-replace ships broken cryptography. OpenRewrite handles this correctly; manual approaches risk corruption.
- **Spring Security 5→6 API changes:** Consistently cited as the single most painful aspect. Lambda DSL replaces method-chaining config. Not directly relevant to this project (no auth layer), but worth noting for the presentation as a general migration concern.
- **Circular dependency injection:** Works silently in Spring Boot 1.5, rejected by default in Spring Boot 2.6+. May require architectural refactoring, not just config changes.
- **Spring Data findOne() removed:** Not deprecated, fully removed in Spring Data 2.0+. Must replace with `findById(id).orElse(null)` or `.orElseThrow()`.
- **H2 dialect changes:** H2 database behavior changed between versions managed by Boot 1.5 vs 3.x. DDL auto-generation and SQL syntax may differ subtly.
- **Jackson serialization defaults changed** between Boot 1.5 and 3.x — date formatting, null handling, and property naming may silently produce different JSON output. Regression risk.
- **Non-ASCII HTTP header handling** changed in Spring Boot 3.0-3.3, causing subtle runtime exceptions that may not surface until edge cases hit.

## Phase 1: Spring Boot 1.5 → 2.7 (Key Changes)

- Spring Boot 2.x still uses `javax.*` namespace — this phase focuses on framework API changes, not namespace
- `server.context-path` → `server.servlet.context-path`
- Spring Data: `findOne(id)` → `findById(id).orElse(null)`
- Hibernate 5.0 → 5.6 (still javax.persistence)
- Actuator endpoint paths restructured
- Property binding changes (relaxed binding rules tightened)
- JUnit 4 can still run via vintage engine, but migration to JUnit 5 recommended here

## Phase 2: Spring Boot 2.7 → 3.x (Key Changes)

- **javax.* → jakarta.* namespace migration** (the big one)
- Java 17 minimum required (we go to 25)
- Hibernate 5.6 → 6.x (new SQL type mappings)
- Springfox Swagger 2.x → SpringDoc OpenAPI 3 (Springfox is incompatible with Boot 3.x)
- Spring Security 6.x (not directly relevant — no auth in this project)
- Auto-configuration registration changed (META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports)

## Phase 3: Containerization

- Convert WAR packaging to executable JAR (remove maven-war-plugin, remove provided scope from Tomcat starter)
- Remove `SpringBootServletInitializer` extends, remove web.xml
- Add Paketo buildpack via `spring-boot-maven-plugin` (`mvn spring-boot:build-image`)
- Paketo auto-detects Spring Boot, installs BellSoft Liberica JVM, layers image for efficient caching
- Generates OCI-compliant container image — no Dockerfile needed

## Code Modernization Inventory

- **BookService.java:** Anonymous `Comparator` → lambda expression in `getBooksSortedByAuthor()`
- **LoanService.java:** Manual for-loop filtering → Stream API in `getOverdueLoans()`, `getActiveLoans()`
- **LoanService.java:** `Calendar.add(-14, days)` → `LocalDate.now().minusDays(14)` for due date calculation
- **Book.java:** `SimpleDateFormat` → `DateTimeFormatter` in `getFormattedPublishedDate()`
- **Book.java, Member.java, Loan.java:** `java.util.Date` / `Calendar` fields → `LocalDate` / `LocalDateTime`
- **All models:** `@Temporal(TemporalType.DATE)` → removed (not needed with java.time types)
- **LoanController.java:** Manual `HashMap<String,String>` error responses → consider `@RestControllerAdvice` with `@ExceptionHandler`
- **All controllers:** Explicit generic types `new ResponseEntity<Book>()` → diamond operator or `ResponseEntity.ok()`
- **Test classes:** `@RunWith(SpringRunner.class)` → `@ExtendWith(SpringExtension.class)`, `@Test` import from org.junit.jupiter.api

## Available Migration Tools

- **OpenRewrite / Moderne:** Automated refactoring engine. Curated recipes for Java 8→25, javax→jakarta, Spring Boot 1.5→3.x. Covers ~80-95% of changes. Apache 2.0 licensed. Consider demonstrating in presentation as complementary to BMAD.
- **IntelliJ IDEA:** Built-in "Migrate to Jakarta EE" refactoring. Handles ~95% of javax→jakarta imports. IDE-scoped only.
- **Eclipse Transformer:** Binary-level bytecode transformation (javax→jakarta at JAR level). Stopgap for third-party libs without jakarta versions.
- **Spring Boot Migrator:** Experimental CLI from Spring team. Scans Boot 2.x projects, generates migration report. Limited adoption.

## Market Context (for presentation narrative)

- ~51% of Java developers still on legacy versions (Java 8, 11, or older) — Azul 2025 Survey
- Java 8 EOL: November 2026 — hard security deadline, no more public patches
- Spring Boot 1.5 EOL since August 2019; Boot 2.x open-source support also ended
- 90% of Fortune 500 still use Java for enterprise apps
- One team reported 40% performance improvement and handling 2x traffic with half the resources after Java 8→21 migration
- 88% of enterprises considering leaving Oracle Java for alternatives (cost, open-source preference, licensing uncertainty)
- Developers using AI coding assistants for migration report meaningful productivity gains in repetitive refactoring

## Presentation Context

- **Audience:** Developers — show them how BMAD works in practice
- **Dual message:** (A) BMAD helps structure technical migrations, (B) This is how to modernize brownfield projects with AI
- **Format:** Reveal.js — code as presentation, developer-friendly
- **Live demo points:** 1-2 BMAD steps run live for "wow factor", rest shown as pre-generated artifacts
- **Key demo moment:** The javax.crypto trap — BMAD catches what naive approaches miss
- **BMAD artifacts as living docs:** Not just planning artifacts — they become onboarding material and decision audit trail for the modernized codebase

## Rejected Ideas

- **Direct 1.5→3.x jump:** Rejected in favor of phased approach. Spring's own migration guides recommend intermediate steps. Skipping 2.x means absorbing two major breaking changes simultaneously. The phased approach also makes for a better presentation — each phase is a visible milestone.
- **GraalVM native-image in v1:** Too much complexity for the demo scope. Natural Phase 2 after core migration is stable.
- **Production database:** H2 in-memory is sufficient. No need for PostgreSQL/MySQL for demo purposes.
- **CI/CD pipeline:** Out of scope — would dilute the BMAD methodology focus.
- **Authentication/authorization:** No auth layer exists, and adding one would expand scope beyond the migration story.

## Open Questions

- Which Spring Boot 3.x minor version to target? (3.4.x is latest stable as of April 2026)
- Should OpenRewrite be used as part of the migration or kept purely manual for demonstration purposes?
- Paketo buildpack base image: BellSoft Liberica (default) or switch to Eclipse Temurin?
- Should the devcontainer be updated to support both Java 8 (before) and Java 25 (after) for side-by-side comparison in the presentation?
