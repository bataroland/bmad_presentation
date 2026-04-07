---
stepsCompleted:
  - step-01-validate-prerequisites
  - step-02-design-epics
  - step-03-create-stories
  - step-04-final-validation
status: complete
completedAt: '2026-04-07'
inputDocuments:
  - _bmad-output/planning-artifacts/prd.md
  - _bmad-output/planning-artifacts/architecture.md
---

# Library API Migration - Epic Breakdown

## Overview

This document provides the complete epic and story breakdown for Library API Migration, decomposing the requirements from the PRD and Architecture into implementable stories.

## Requirements Inventory

### Functional Requirements

- FR1: Developer can migrate Spring Boot version from 1.5 to 2.7 with all framework API changes applied and a green build
- FR2: Developer can migrate Spring Boot version from 2.7 to 3.x with all namespace and Hibernate changes applied and a green build
- FR3: Developer can convert WAR packaging to executable JAR and build a Paketo OCI container image
- FR4: Developer can verify each migration phase independently via build + test suite before proceeding to the next phase
- FR5: Developer can roll back a failed migration phase by reverting the corresponding git branch
- FR6: Developer can migrate all javax.* annotations to jakarta.* equivalents across entities and controllers, verified by namespace purity check (JDK-owned packages like javax.crypto excluded)
- FR7: Developer can replace all `findOne()` repository calls with `findById().orElse()` or `.orElseThrow()`
- FR8: Developer can modernize pre-Java 8 code idioms to current standards (anonymous classes → lambdas, manual loops → streams, explicit generics → diamond operator)
- FR9: Developer can migrate all date/time handling from java.util.Date/Calendar/SimpleDateFormat to java.time.* equivalents with per-field type mapping (LocalDate for date-only fields, LocalDateTime for timestamps)
- FR10: Developer can centralize error handling in a `@RestControllerAdvice` class replacing duplicated try/catch blocks
- FR11: System returns structured JSON error responses (`{"error": "message", "status": code}`) for all exception types
- FR12: System preserves original error behavior for `IllegalArgumentException` (404) and `IllegalStateException` (400)
- FR13: Developer can migrate all test classes from JUnit 4 to JUnit 5 (Jupiter annotations, extensions)
- FR14: All existing tests pass on the migrated codebase with identical assertions
- FR15: Developer can replace Springfox Swagger 2 with SpringDoc OpenAPI 3 as the API documentation framework
- FR16: System exposes Swagger UI at `/swagger-ui/index.html` with all 12 endpoints documented
- FR17: System initializes seed data correctly on startup regardless of Spring Boot version (data.sql executes after schema creation)
- FR18: Developer can configure explicit Jackson date format and timezone to preserve response parity
- FR19: System loads seed data from `data.sql` on startup with identical data as the original version
- FR20: All 12 REST endpoints return identical JSON response structure and values as the original version (validated independently of unit tests — test passage alone does not guarantee response parity)
- FR21: Containerized application runs all 12 endpoints with response parity to the JAR version
- FR22: BMAD workflow produces complete artifact set (Product Brief, PRD, Architecture, Epics & Stories) with cross-document traceability, explicit decision records, and specific file/code references
- FR23: Epics and stories are specific enough that a developer unfamiliar with the project can execute them using only the story description and the codebase
- FR24: Presenter can show a Reveal.js presentation covering the full migration journey with narrative arc
- FR25: Presenter can switch between original (Java 8) and migrated (Java 25) applications live during the presentation, with both running locally without network dependency
- FR26: Presenter can demonstrate at least 2 BMAD workflow phases live
- FR27: Presenter can show an end-to-end traceability walkthrough (story → epic → architecture → PRD → brief)
- FR28: Presentation follows narrative arc (pain recognition → BMAD walkthrough → live demo → "try it yourself" with tooling prerequisites) and includes time investment estimate and "without BMAD" contrast
- FR29: Audience member can access a one-page quick reference card with BMAD workflow steps and migration pitfalls
- FR30: Post-presentation explorer can clone the demo repository and run both original and migrated versions using only the README
- FR31: Demo repository contains all BMAD artifacts alongside the code for self-guided exploration
- FR32: Demo repository provides a clean, reviewable diff between original and migrated code (via git branches or side-by-side folders)
- FR33: Demo repository README includes troubleshooting section for common setup issues (container runtime, Java version, Maven)
- FR34: Demo repository README includes suggested reading order for BMAD artifacts with one-line explanation of what each teaches

### NonFunctional Requirements

- NFR1: Each migration phase completes with zero compiler warnings in project source code (src/main/java, src/test/java) when compiled with the target JDK (Java 25) — framework-internal deprecation warnings excluded
- NFR2: Migrated code uses no migration workarounds (@Lazy for circular dependencies, Spring compatibility flags, javax compatibility shims) — all breaking changes are resolved properly, not suppressed
- NFR3: Reveal.js presentation loads and renders correctly without network access
- NFR4: Both original and migrated applications can run simultaneously on different ports (original: 8081, migrated: 8082 — documented in README)
- NFR5: Demo repository builds successfully with a single `mvn` command on any JDK 17+, no manual dependency setup or environment configuration beyond JDK installation
- NFR6: README quick start requires no more than 3 commands to run in embedded mode (clone, cd, mvn spring-boot:run). Container mode documented separately.

### Additional Requirements (from Architecture)

- No starter template — brownfield migration of existing codebase
- Phased migration: Boot 1.5 → 2.7.18 → 3.4.5 with phase gates (ADR-001)
- Git branch per phase: main (original), phase-1/boot-2.7, phase-2/boot-3.x, phase-3/containerize (ADR-005)
- Springfox Boot 2.6+ workaround: add ant_path_matcher in Phase 1, remove in Phase 2 (Architecture Decision Point)
- Testing strategy: migrate tests BEFORE code changes in Phase 1. Vintage engine fallback if needed. Mockito 1.x→4.x strict stubbing.
- Jackson response spot-check required in Phase 1 (not just Phase 2) — Boot 2.7 may change serialization defaults
- Phase 1 pom.xml: remove explicit maven-compiler-plugin version, add JUnit 5, handle Mockito upgrade
- Phase 2 pom.xml: remove Springfox, add SpringDoc, remove hibernate-validator, add spring-boot-starter-validation, Java 25
- Phase 3 pom.xml: remove WAR packaging, remove war plugin, remove Tomcat provided scope, add Paketo image config
- Phase 3 Java: remove SpringBootServletInitializer extends, delete web.xml
- data.sql: keep data.sql with defer-datasource-initialization=true (not import.sql)
- Error handling: 4 exception types in @RestControllerAdvice (IllegalArgument→404, IllegalState→400, MethodArgumentNotValid→400, generic→500)
- Commit convention: [Phase X] type: description
- Demo runtime: SDKMAN JDK switching, port 8081 (original) + 8082 (migrated via -Dserver.port)
- Devcontainer: update to Java 25 after Phase 3
- Tracked temporary exception: ant_path_matcher must be explicitly removed in Phase 2 with verification grep

### UX Design Requirements

Not applicable — REST API backend, no UI components.

### FR Coverage Map

| FR | Epic | Description |
|----|------|------------|
| FR1 | Epic 1 | Boot 1.5→2.7 migration |
| FR2 | Epic 2 | Boot 2.7→3.x migration |
| FR3 | Epic 3 | WAR→JAR + Paketo |
| FR4 | Cross-cutting | Phase gate verification — applied at Epic 1, 2, 3 boundaries |
| FR5 | Epic 1 | Branch rollback strategy |
| FR6 | Epic 2 | javax→jakarta namespace |
| FR7 | Epic 1 | findOne→findById |
| FR8 | Epic 2 | Code idiom modernization (lambdas, streams) |
| FR9 | Epic 2 | Date/time migration (java.time) |
| FR10 | Epic 1 | @RestControllerAdvice centralization |
| FR11 | Epic 1 | Structured error responses |
| FR12 | Epic 1 | Error behavior preservation |
| FR13 | Epic 1 | JUnit 4→5 migration |
| FR14 | Epic 1 | Tests pass on migrated codebase |
| FR15 | Epic 2 | Springfox→SpringDoc |
| FR16 | Epic 2 | Swagger UI on SpringDoc |
| FR17 | Epic 1 | data.sql initialization fix |
| FR18 | Epic 2 | Jackson date format config |
| FR19 | Epic 1 | Seed data loaded correctly |
| FR20 | Epic 1, 2 | Response parity (error in E1, full in E2) |
| FR21 | Epic 3 | Container response parity |
| FR22 | Epic 4 | BMAD artifact completeness |
| FR23 | Epic 4 | Story reproducibility |
| FR24 | Epic 4 | Reveal.js presentation |
| FR25 | Epic 4 | Live demo switching |
| FR26 | Epic 4 | Live BMAD demo |
| FR27 | Epic 4 | Traceability walkthrough |
| FR28 | Epic 4 | Narrative arc + contrast |
| FR29 | Epic 4 | Quick reference card |
| FR30 | Epic 4 | Demo repo clone & run |
| FR31 | Epic 4 | BMAD artifacts in repo |
| FR32 | Epic 4 | Clean code diff |
| FR33 | Epic 4 | README troubleshooting |
| FR34 | Epic 4 | README reading order |

**Coverage: 34/34 FR — 100%**

## Epic List

### Epic 1: Spring Boot 2.7 Migration (Phase 1)

The application runs on Spring Boot 2.7 with JUnit 5 tests, centralized error handling, and verified response parity. Phase 1 Gate passed.

**FRs covered:** FR1, FR5, FR7, FR10, FR11, FR12, FR13, FR14, FR17, FR19, FR20 (partial — error response parity)
**NFRs addressed:** NFR1, NFR2
**Phase gate:** PHASE 1 GATE — green build + response spot-check (3+ endpoints) + H2 data verified
**Note:** First story must be repository/branch setup per ADR-005
**Priority ordering:** Critical path: branch setup → JUnit 5 migration → Boot 2.7 upgrade (these block everything). Parallelizable after Boot 2.7 green build: findOne, context-path, data.sql, error handling. Last: response spot-check.

### Epic 2: Spring Boot 3.x Migration & Code Modernization (Phase 2)

The application runs on Java 25 / Spring Boot 3.x with jakarta namespace, SpringDoc OpenAPI, modern Java idioms (lambdas, streams, java.time), and verified full response parity. Phase 2 Gate passed.

**FRs covered:** FR2, FR6, FR8, FR9, FR15, FR16, FR18, FR20 (full response parity)
**NFRs addressed:** NFR1, NFR2
**Architecture tracked exception:** ant_path_matcher removal — explicit story required, Phase 2 gate verifies grep returns 0
**Story ordering constraint:** namespace migration stories MUST compile before java.time stories — both touch entity files
**Phase gate:** Intermediate after namespace (green build + grep clean), then PHASE 2 GATE after code modernization — green build + full response parity check + namespace grep clean
**Story ordering:** 1. pom.xml upgrade (Boot 3.4.5 + Java 25 + dependency swaps) → 2. javax→jakarta namespace + grep → 3. SpringDoc migration (remove Springfox + ant_path_matcher) → 4. java.time migration (verify data.sql INSERT formats match LocalDateTime columns) → 5. Code modernization (lambdas, streams) → 6. Jackson config + full response parity check
**Depends on:** Epic 1 (Phase 1 Gate must pass)

### Epic 3: Containerization (Phase 3)

The application runs as a Paketo OCI container with response parity to the JAR version. Phase 3 Gate passed.

**FRs covered:** FR3, FR21
**NFRs addressed:** NFR4, NFR5
**Phase gate:** PHASE 3 GATE — container response parity with JAR version
**Prerequisite:** Verify Podman socket is mounted in devcontainer (devcontainer.json configures /run/podman/podman.sock). If not available, build Paketo image outside devcontainer.
**Depends on:** Epic 2 (Phase 2 Gate must pass)

### Epic 4: Presentation & Demo Deliverables

A complete Reveal.js presentation with live demo, quick reference card, and self-contained demo repository are ready for the audience.

**FRs covered:** FR22, FR23, FR24, FR25, FR26, FR27, FR28, FR29, FR30, FR31, FR32, FR33, FR34
**NFRs addressed:** NFR3, NFR4, NFR6
**First story:** Create presentation outline (slide-by-slide content plan with BMAD artifact references) BEFORE coding Reveal.js slides. Use Caravaggio agent for narrative structure.
**Depends on:** Epic 1-3 (requires completed migration for live demo and code diffs)

## Epic 1: Spring Boot 2.7 Migration (Phase 1)

The application runs on Spring Boot 2.7 with JUnit 5 tests, centralized error handling, and verified response parity. Phase 1 Gate passed.

### Story 1.1: Repository & Branch Setup

As a developer,
I want to set up the git branch strategy for phased migration,
So that each phase has an isolated branch with rollback capability.

**Acceptance Criteria:**

**Given** the main branch contains the original Java 8 / Boot 1.5 code
**When** I create branch `phase-1/boot-2.7` from main
**Then** the branch exists and contains identical code to main
**And** `mvn clean test` passes on the new branch
**And** the original state on main is preserved as reference

### Story 1.2: JUnit 5 Test Migration

As a developer,
I want to migrate all test classes from JUnit 4 to JUnit 5,
So that the test framework is ready for Spring Boot 2.7.

**Acceptance Criteria:**

**Given** tests use `@RunWith(SpringRunner.class)` and `org.junit.Test`
**When** I migrate to `@ExtendWith(SpringExtension.class)` and `org.junit.jupiter.api.Test`
**Then** all tests pass with JUnit 5 Jupiter
**And** no JUnit 4 imports remain in test source files
**And** no vintage engine dependency is needed in pom.xml
**And** Mockito unused stubs are removed (strict stubbing compatibility)

### Story 1.3: Spring Boot 2.7 Parent Upgrade

As a developer,
I want to upgrade the Spring Boot parent from 1.5.22 to 2.7.18,
So that the application uses the latest Boot 2.x framework.

**Acceptance Criteria:**

**Given** pom.xml has `spring-boot-starter-parent` version `1.5.22.RELEASE`
**When** I change to version `2.7.18` and add Springfox workaround `spring.mvc.pathmatch.matching-strategy=ant_path_matcher`
**Then** `mvn clean compile` succeeds with zero warnings in project source
**And** explicit `maven-compiler-plugin` version is removed (Boot parent manages)
**And** Swagger UI at `/library/swagger-ui.html` loads correctly
**And** the workaround is documented as temporary in the commit message: `[Phase 1] config: add ant_path_matcher workaround (temporary — removed in Phase 2)`

### Story 1.4: Configuration & Repository API Migration

As a developer,
I want to update configuration properties and repository API calls for Boot 2.7 compatibility,
So that the application starts correctly and data access works.

**Acceptance Criteria:**

**Given** `application.properties` uses `server.context-path=/library`
**When** I rename to `server.servlet.context-path=/library` and add `spring.jpa.defer-datasource-initialization=true`
**Then** the application starts on port 8081 with context path `/library`
**And** `data.sql` seed data loads correctly (5 books, members visible in H2 console)
**And** all `findOne(id)` calls in services are replaced with `findById(id).orElse(null)` or `.orElseThrow()`
**And** `mvn clean test` passes

### Story 1.5: Error Handling Centralization

As a developer,
I want to centralize error handling in a `@RestControllerAdvice` class,
So that all error responses are consistent and controllers are simplified.

**Acceptance Criteria:**

**Given** `LoanController` has duplicated try-catch blocks with `HashMap<String,String>` responses
**When** I create `config/GlobalExceptionHandler.java` with `@RestControllerAdvice` and remove all try-catch blocks from `LoanController`
**Then** `IllegalArgumentException` returns 404 with `{"error": "message", "status": 404}`
**And** `IllegalStateException` returns 400 with `{"error": "message", "status": 400}`
**And** `MethodArgumentNotValidException` returns 400 with concatenated field errors
**And** unhandled exceptions return 500 with `{"error": "message", "status": 500}`
**And** `LoanController` methods are simplified one-liners delegating to service layer
**And** `mvn clean test` passes

### Story 1.6: Phase 1 Gate Validation

As a developer,
I want to verify Phase 1 migration is complete and correct,
So that I can proceed to Phase 2 with confidence.

**Acceptance Criteria:**

**Given** all Phase 1 stories (1.1-1.5) are completed
**When** I run the Phase 1 gate checks
**Then** `mvn clean test` passes with zero compiler warnings in project source
**And** API response spot-check: GET `/api/books`, GET `/api/books/1`, GET `/api/loans/active` return identical JSON to Boot 1.5 version (compare manually)
**And** H2 console at `/library/h2-console` shows all 3 tables with seed data
**And** Swagger UI at `/library/swagger-ui.html` shows all 12 endpoints
**And** no migration workarounds exist except the documented temporary ant_path_matcher
**And** commit convention followed: all commits use `[Phase 1] type: description` format

## Epic 2: Spring Boot 3.x Migration & Code Modernization (Phase 2)

The application runs on Java 25 / Spring Boot 3.x with jakarta namespace, SpringDoc OpenAPI, modern Java idioms (lambdas, streams, java.time), and verified full response parity. Phase 2 Gate passed.

### Story 2.1: Spring Boot 3.x Parent Upgrade & Dependency Migration

As a developer,
I want to upgrade Spring Boot from 2.7.18 to 3.4.5 with Java 25,
So that the application is on the latest LTS framework.

**Acceptance Criteria:**

**Given** pom.xml has `spring-boot-starter-parent` version `2.7.18` and `java.version` `1.8`
**When** I change parent to `3.4.5`, set `java.version` to `25`, remove `springfox-swagger2` and `springfox-swagger-ui`, add `springdoc-openapi-starter-webmvc-ui` (verify latest compatible version), remove explicit `hibernate-validator`, and add `spring-boot-starter-validation`
**Then** `mvn clean compile` succeeds (namespace errors expected — resolved in next story)
**And** commit message: `[Phase 2] migrate: upgrade Spring Boot parent to 3.4.5, Java 25`

### Story 2.2: javax to jakarta Namespace Migration

As a developer,
I want to migrate all javax.* annotations to jakarta.* equivalents,
So that the codebase is compatible with Spring Boot 3.x.

**Acceptance Criteria:**

**Given** entities use `javax.persistence.*` and controllers use `javax.validation.*`
**When** I replace all javax imports with jakarta equivalents in `model/`, `controller/`, and `config/` packages
**Then** `mvn clean compile` succeeds
**And** namespace purity grep returns 0 results: `grep -r "import javax\." src/main/java/ | grep -v "javax.crypto" | grep -v "javax.net" | grep -v "javax.security"`
**And** no JDK-owned javax packages (javax.crypto, javax.net.ssl) were accidentally renamed
**And** `mvn clean test` passes
**And** commit message: `[Phase 2] migrate: javax.persistence → jakarta.persistence all entities and controllers`

### Story 2.3: SpringDoc Migration & Springfox Removal

As a developer,
I want to replace Springfox Swagger 2 with SpringDoc OpenAPI 3,
So that API documentation works on Spring Boot 3.x.

**Acceptance Criteria:**

**Given** `SwaggerConfig.java` exists with `@EnableSwagger2` and Springfox Docket bean
**When** I delete `SwaggerConfig.java`, remove `spring.mvc.pathmatch.matching-strategy=ant_path_matcher` from `application.properties`, and add SpringDoc properties (`springdoc.info.title=Library API v2 — Java 25 / Spring Boot 3.x`)
**Then** Swagger UI at `/library/swagger-ui/index.html` shows all 12 endpoints
**And** OpenAPI spec at `/library/v3/api-docs` returns valid JSON
**And** `application.properties` contains no `ant_path_matcher` reference (grep verification)
**And** no Springfox imports remain anywhere in the codebase
**And** commit message: `[Phase 2] migrate: Springfox → SpringDoc OpenAPI 3, remove ant_path_matcher workaround`

### Story 2.4: Date/Time Migration to java.time

As a developer,
I want to migrate all date/time handling from java.util.Date/Calendar to java.time,
So that the codebase uses modern, thread-safe date/time APIs.

**Acceptance Criteria:**

**Given** entities use `java.util.Date`, `Calendar`, `SimpleDateFormat`, and `@Temporal` annotations
**When** I migrate per ADR-003 field mapping:
- `Book.publishedDate` → `LocalDate`
- `Book.createdAt` → `LocalDateTime`
- `Member.membershipDate` → `LocalDate`
- `Loan.borrowDate`, `dueDate`, `returnDate` → `LocalDate`
- `Book.getFormattedPublishedDate()`: `SimpleDateFormat` → `DateTimeFormatter`
- `LoanService`: `Calendar.add(-14, days)` → `LocalDate.now().minusDays(14)`
**Then** all `@Temporal` annotations are removed
**And** no `java.util.Date`, `Calendar`, or `SimpleDateFormat` imports remain in `model/` and `service/` packages
**And** `data.sql` INSERT statements are compatible with new column types (verify `createdAt` LocalDateTime format)
**And** `mvn clean test` passes
**And** commit message: `[Phase 2] migrate: java.util.Date/Calendar → java.time per ADR-003 mapping`

### Story 2.5: Code Idiom Modernization

As a developer,
I want to modernize pre-Java 8 code idioms to current standards,
So that the codebase uses modern, readable Java patterns.

**Acceptance Criteria:**

**Given** `BookService` uses anonymous `Comparator` and `LoanService` uses manual for-loops
**When** I modernize:
- `BookService.getBooksSortedByAuthor()`: anonymous Comparator → lambda
- `LoanService.getOverdueLoans()`: manual for-loop → Stream API filter
- `LoanService.getActiveLoans()`: manual for-loop → Stream API filter
- All controllers: `new ResponseEntity<Book>(book, HttpStatus.OK)` → `ResponseEntity.ok(book)` or diamond operator
**Then** no anonymous inner classes remain in service layer
**And** no manual for-loop filtering remains where Stream API is applicable
**And** `mvn clean test` passes
**And** commit message: `[Phase 2] refactor: modernize code idioms (lambdas, streams, diamond operator)`

### Story 2.6: Jackson Configuration & Phase 2 Gate Validation

As a developer,
I want to configure explicit Jackson serialization and validate full response parity,
So that the migrated API produces identical output to the original.

**Acceptance Criteria:**

**Given** all Phase 2 code changes are complete (stories 2.1-2.5)
**When** I verify/add `spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss` and `spring.jackson.time-zone=Europe/Budapest` in `application.properties`
**Then** `mvn clean test` passes with zero compiler warnings in project source
**And** all 12 REST endpoints return JSON responses compared manually with the Boot 1.5 original version:
- GET `/api/books` — book list with correct date format
- GET `/api/books/1` — single book
- POST `/api/books` (with valid body) — created response
- GET `/api/loans/active` — active loans
- GET `/api/loans/overdue` — overdue loans (date calculation correct)
- POST `/api/loans/borrow?bookId=1&memberId=1` — borrow response
**And** `LocalDate` fields serialize as `yyyy-MM-dd` (accepted intentional improvement — no `T00:00:00` suffix)
**And** `LocalDateTime` fields serialize as `yyyy-MM-dd'T'HH:mm:ss`
**And** namespace grep clean: 0 results for `javax.persistence` or `javax.validation`
**And** no `ant_path_matcher` in application.properties
**And** all commits use `[Phase 2] type: description` format

## Epic 3: Containerization (Phase 3)

The application runs as a Paketo OCI container with response parity to the JAR version. Phase 3 Gate passed.

### Story 3.1: WAR to JAR Conversion

As a developer,
I want to convert the application from WAR to executable JAR packaging,
So that it can run standalone without an external Tomcat server.

**Acceptance Criteria:**

**Given** pom.xml has `<packaging>war</packaging>`, maven-war-plugin, and spring-boot-starter-tomcat with `<scope>provided</scope>`
**When** I remove WAR packaging line, remove maven-war-plugin, remove the entire spring-boot-starter-tomcat provided dependency block, remove `extends SpringBootServletInitializer` from `LibraryApplication.java`, and delete `src/main/webapp/WEB-INF/web.xml`
**Then** `mvn clean package` produces `target/library-api.jar` (not .war)
**And** `java -jar target/library-api.jar` starts the application on port 8081
**And** all 12 endpoints respond correctly via Swagger UI at `/library/swagger-ui/index.html`
**And** `mvn clean test` passes
**And** commit message: `[Phase 3] migrate: convert WAR packaging to executable JAR`

### Story 3.2: Paketo Buildpack Container Image

As a developer,
I want to build an OCI container image using Paketo buildpacks,
So that the application can be deployed as a container without a Dockerfile.

**Acceptance Criteria:**

**Given** the application builds as an executable JAR (Story 3.1 complete)
**When** I add Paketo image configuration to `spring-boot-maven-plugin` (`<image><name>library-api:${project.version}</name></image>`) and run `mvn spring-boot:build-image`
**Then** a container image `library-api:1.0.0` is created successfully
**And** `docker run -p 8082:8081 library-api:1.0.0` starts the application
**And** Swagger UI at `localhost:8082/library/swagger-ui/index.html` shows all 12 endpoints
**And** if using Podman: `DOCKER_HOST=unix:///run/podman/podman.sock` is set before build
**And** commit message: `[Phase 3] migrate: add Paketo buildpack configuration`

### Story 3.3: Phase 3 Gate — Container Response Parity Validation

As a developer,
I want to verify the containerized application produces identical responses to the JAR version,
So that I can confirm containerization didn't change behavior.

**Acceptance Criteria:**

**Given** the JAR version runs on port 8081 and the container runs on port 8082
**When** I compare API responses between both versions for key endpoints:
- GET `/api/books` — book list
- GET `/api/books/1` — single book
- POST `/api/loans/borrow?bookId=2&memberId=1` — borrow response
- GET `/api/loans/active` — active loans
- GET `/api/loans/overdue` — overdue loans
**Then** all responses are identical in structure, field names, values, and date formatting
**And** `mvn clean test` passes in the containerized environment
**And** the `phase-3/containerize` branch is ready to merge to main
**And** devcontainer Dockerfile is updated to Java 25 (from Java 8)
**And** all commits use `[Phase 3] type: description` format

## Epic 4: Presentation & Demo Deliverables

A complete Reveal.js presentation with live demo, quick reference card, and self-contained demo repository are ready for the audience.

### Story 4.1: Presentation Outline & Narrative Design

As a presenter,
I want a slide-by-slide content plan with BMAD artifact references,
So that the Reveal.js presentation has a clear narrative arc before coding begins.

**Acceptance Criteria:**

**Given** all BMAD artifacts exist (Brief, PRD, Architecture, Epics) and migration is complete
**When** I create a presentation outline document listing each slide's title, content source, and speaker notes
**Then** the outline follows the narrative arc: pain recognition → BMAD walkthrough → live demo → "try it yourself"
**And** at least 2 slides are marked as "live demo point" (BMAD phase demo + migrated app demo)
**And** one slide contains the traceability walkthrough (story → epic → architecture → PRD → brief)
**And** one slide contains the "without BMAD" contrast
**And** one slide shows time investment estimate
**And** the closing slide contains "try it yourself" with tooling prerequisites (Claude Code, BMAD installation)
**And** the Caravaggio presentation agent is consulted for narrative structure

### Story 4.2: Reveal.js Presentation Implementation

As a presenter,
I want a working Reveal.js presentation with all slides coded,
So that I can present the BMAD migration story to a developer audience.

**Acceptance Criteria:**

**Given** the presentation outline (Story 4.1) is approved
**When** I create the Reveal.js HTML/markdown presentation
**Then** the presentation loads in a browser without network access (NFR3)
**And** code samples use syntax highlighting and are readable on projected display
**And** before/after code comparisons are visible side-by-side
**And** navigation works with keyboard (arrow keys, space)
**And** speaker notes are available (press 'S' in Reveal.js)
**And** the presentation includes at least 15 slides covering the full narrative arc
**And** commit message: `[Phase 4] docs: create Reveal.js presentation`

### Story 4.3: Live Demo Setup & Verification

As a presenter,
I want both original and migrated applications running simultaneously for live switching,
So that the audience sees real API responses during the presentation.

**Acceptance Criteria:**

**Given** the original app (Java 8, Boot 1.5) and migrated app (Java 25, Boot 3.x) exist
**When** I set up the demo runtime using SDKMAN:
- Terminal 1: `sdk use java 8.0.482-tem && mvn spring-boot:run` (port 8081)
- Terminal 2: `sdk use java 25-tem && mvn spring-boot:run -Dserver.port=8082` (port 8082)
**Then** both applications are accessible simultaneously
**And** Swagger UI works on both: `localhost:8081/library/swagger-ui.html` (original) and `localhost:8082/library/swagger-ui/index.html` (migrated)
**And** the Swagger UI titles visually differentiate the versions ("Library API" vs "Library API v2 — Java 25 / Spring Boot 3.x")
**And** switching between browser tabs demonstrates the migration instantly
**And** `sdk use` is used (session-scoped), NOT `sdk default` (global)

### Story 4.4: BMAD Quick Reference Card

As an audience member,
I want a one-page quick reference card with BMAD workflow steps and migration pitfalls,
So that I can start using BMAD on my own project Monday morning.

**Acceptance Criteria:**

**Given** the complete BMAD workflow has been demonstrated (Document Project → Brief → PRD → Architecture → Epics)
**When** I create a markdown quick reference card
**Then** the card fits on one page (printed A4)
**And** it lists all 5 BMAD workflow steps with one-line description and the slash command (`/bmad-document-project`, etc.)
**And** it lists the top 5 migration pitfalls discovered by BMAD (javax.crypto trap, circular dependency, Jackson serialization, data.sql ordering, Springfox Boot 2.6+ incompatibility)
**And** it includes "Getting Started" section: Claude Code install → BMAD install → first command
**And** commit message: `[Phase 4] docs: create BMAD quick reference card`

### Story 4.5: Demo Repository README & Documentation

As a post-presentation explorer,
I want a comprehensive README with quick start, troubleshooting, and BMAD artifact guide,
So that I can clone the repo and explore independently without asking Roland.

**Acceptance Criteria:**

**Given** the demo repository contains both original and migrated code (via git branches) and all BMAD artifacts
**When** I create/update the README.md
**Then** quick start requires no more than 3 commands: `git clone`, `cd`, `mvn spring-boot:run` (NFR6)
**And** README documents both versions: original (main branch, Boot 1.5) and migrated (latest branch, Boot 3.x)
**And** troubleshooting section covers: Podman `DOCKER_HOST` setup, Java version mismatch, Maven dependency issues
**And** suggested BMAD artifact reading order: Brief → PRD → Architecture → Epics (with one-line explanation of what each teaches)
**And** README documents demo runtime options (SDKMAN switch, container mode)
**And** builds successfully with single `mvn` command on any JDK 17+ (NFR5)
**And** commit message: `[Phase 4] docs: create comprehensive README with quick start and troubleshooting`

### Story 4.6: Demo Repository Final Polish & Traceability Verification

As a presenter,
I want the demo repository to be self-contained and the traceability chain verified,
So that Gergő-type explorers find substance, not templates.

**Acceptance Criteria:**

**Given** all migration epics (1-3) complete and presentation materials (4.1-4.5) ready
**When** I perform final repository polish
**Then** all BMAD artifacts are present in `_bmad-output/planning-artifacts/` alongside the code
**And** git branches provide clean, reviewable diffs: `git diff main..phase-1/boot-2.7`, `git diff phase-1/boot-2.7..phase-2/boot-3.x`, etc.
**And** the traceability walkthrough works end-to-end: at least one story traces back through epic → architecture decision → PRD requirement → product brief problem statement
**And** BMAD artifacts contain explicit decision records and file references (not template-level output — FR22)
**And** the `[X] hours` placeholder in PRD Success Criteria is filled with actual BMAD planning time
**And** the repository is ready for the presentation rehearsal
