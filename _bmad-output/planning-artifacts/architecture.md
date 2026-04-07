---
stepsCompleted:
  - step-01-init
  - step-02-context
  - step-03-starter
  - step-04-decisions
  - step-05-patterns
  - step-06-structure
  - step-07-validation
  - step-08-complete
status: 'complete'
completedAt: '2026-04-07'
inputDocuments:
  - _bmad-output/planning-artifacts/prd.md
  - _bmad-output/planning-artifacts/product-brief-library-api-migration.md
  - _bmad-output/planning-artifacts/product-brief-library-api-migration-distillate.md
  - docs/architecture.md
  - docs/source-tree-analysis.md
  - docs/api-contracts.md
  - docs/data-models.md
  - docs/development-guide.md
workflowType: 'architecture'
project_name: 'Library API Migration'
user_name: 'Roland'
date: '2026-04-07'
---

# Architecture Decision Document

_This document builds collaboratively through step-by-step discovery. Sections are appended as we work through each architectural decision together._

### How to Read This Document

**For understanding the full architecture:** Read top-to-bottom (Context → Technology → Decisions → Patterns → Structure → Validation).

**For implementing a specific phase:** See AI Agent Context Guidance in Validation section — extract only the relevant ~100 lines for your phase.

**For reviewing a specific decision:** Jump to the ADR by number (ADR-001 through ADR-008) in Core Decisions or Technology Foundation sections.

**For quick reference during implementation:** See application.properties Changes and pom.xml Changes in Implementation Patterns section.

## Project Context Analysis

### Requirements Overview

**Functional Requirements:** 34 FRs across 10 capability areas (Migration Execution, Namespace & API, Code Modernization, Error Handling, Test Framework, API Documentation, Data & Configuration, Containerization, BMAD Artifacts, Presentation + Demo Repository).

**Non-Functional Requirements:** 6 NFRs covering migration quality (zero warnings, no workarounds), presentation quality (offline Reveal.js, simultaneous apps), and repository quality (single command build, max 3 commands in README).

**Scale & Complexity:**
- Primary domain: API Backend migration (brownfield)
- Complexity level: Low (14 source files, 3 entities, 12 endpoints)
- Architectural components: Unchanged — 3-tier layered (Controller→Service→Repository→Entity)

### Migration vs. Rewrite Reality

While framed as a migration, the small codebase size means virtually every source file is significantly modified. This is closer to a guided rewrite with the original as a behavioral specification. Architectural implication: response parity cannot be assumed — it must be actively validated at every phase gate, because there is no "untouched" code providing stability.

### Phased Migration — Pedagogical Choice

On a 14-file project, a single OpenRewrite run could achieve most migration changes atomically. The phased approach (1.5→2.7→3.x) is chosen because it makes each category of breaking change visible and isolated — essential for the presentation narrative. On enterprise-scale projects, phasing is a technical necessity, not a choice.

### Target Technology Stack

| Category | Current (Java 8) | Target (Java 25) | Changed In |
|----------|------------------|-------------------|------------|
| Language | Java 1.8 (Temurin) | Java 25 LTS (Temurin) | Phase 2 |
| Framework | Spring Boot 1.5.22 | Spring Boot 3.4.5 | Phase 1→2 |
| Web | Spring MVC 4.3.x | Spring MVC 6.x | Phase 2 |
| ORM | Hibernate 5.0.12 | Hibernate 6.4.x | Phase 2 |
| Database | H2 (managed by Boot) | H2 (managed by Boot) | — |
| Namespace | javax.* | jakarta.* | Phase 2 |
| Validation | Hibernate Validator (javax) | Hibernate Validator (jakarta) | Phase 2 |
| API Docs | Springfox 2.9.2 | SpringDoc OpenAPI 3 | Phase 2 |
| Build | Maven 3.8.7 (WAR) | Maven 3.8.7 (JAR) | Phase 3 |
| App Server | Tomcat 8.5 (embedded) / 9.x (external) | Tomcat 10.x (embedded only) | Phase 3 |
| Test | JUnit 4 + Mockito 1.x | JUnit 5 + Mockito 5.x | Phase 1 |
| Deployment | WAR on Tomcat | Paketo OCI container | Phase 3 |
| Date/Time | java.util.Date, Calendar | java.time.LocalDate, LocalDateTime | Phase 2 |
| Error Handling | Manual HashMap per endpoint | @RestControllerAdvice centralized | Phase 1 |

### Dependency Version Pinning

| Phase | Spring Boot Parent | Java Target | Key Managed Versions |
|-------|-------------------|-------------|---------------------|
| Phase 1 | 2.7.18 (last 2.7.x release) | 1.8 (unchanged) | Hibernate 5.6.x, JUnit 5.8.x, H2 2.1.x |
| Phase 2 | 3.4.5 (latest stable as of April 2026) | 25 | Hibernate 6.4.x, JUnit 5.10.x, H2 2.2.x |
| Phase 3 | 3.4.5 (same as Phase 2) | 25 | — (packaging change only) |

Pin explicitly in pom.xml — do not use version ranges or RELEASE qualifier.

### Technical Constraints & Dependencies

- H2 in-memory database — no persistent storage concerns, but DDL generation and data.sql behavior changes between Boot versions
- No authentication — simplifies migration scope but limits real-world demonstration
- No external service integrations — self-contained application, no API client migration needed
- Podman-based devcontainer — container runtime must support Paketo buildpacks
- Solo developer with 8-day timeline — architecture must minimize ambiguity for implementation

### Cross-Cutting Concerns

- **Response parity**: Every endpoint must produce identical JSON output pre- and post-migration. Spans all layers (entity serialization, service logic, controller response wrapping)
- **Namespace consistency**: javax→jakarta migration touches entities, controllers, and validation. Must be complete — no mixed namespaces
- **Date/time type safety**: Migration from java.util.Date to java.time affects entities, services (arithmetic), and serialization (Jackson). Per-field mapping required
- **Phase isolation**: Each migration phase must be independently buildable, testable, and rollback-able via git branches

## Technology Foundation & Build Configuration

### Starter Template: Not Applicable (Brownfield Migration)

This is a migration of an existing Spring Boot 1.5.22 application, not a greenfield project. The "starter" is the existing `pom.xml` with `spring-boot-starter-parent 1.5.22.RELEASE`. The migration modifies this existing foundation in phases.

### Existing Project Decisions & Migration Impact

| Decision Area | Current (existing project) | Migration Target | Phase |
|--------------|---------------------------|-----------------|-------|
| Language | Java 1.8 | Java 25 | Phase 2 |
| Build tool | Maven 3.8.7 | No change | — |
| Framework | Spring Boot 1.5.22 | 2.7.18 → 3.4.5 | Phase 1→2 |
| Packaging | WAR | JAR | Phase 3 |
| ORM | Spring Data JPA + Hibernate | Version upgrade only | Phase 1→2 |
| Database | H2 in-memory | No change | — |
| API docs | Springfox Swagger 2 | SpringDoc OpenAPI 3 | Phase 2 |
| Testing | JUnit 4 + Mockito 1.x | JUnit 5 + Mockito 4.x | Phase 1 |
| Architecture | 3-tier layered | Preserved (ADR-007) | — |

### pom.xml Changes Per Phase

See Quick Reference Migration Cheat Sheet for implementation checklist format.

**Phase 1 (→ Boot 2.7.18):**
- Parent version: `1.5.22.RELEASE` → `2.7.18`
- Review: remove explicit `maven-compiler-plugin` version if present — let Boot parent manage. Current pom.xml has 3.1, Boot 2.7 provides 3.10+
- Add JUnit 5 Jupiter: `org.junit.jupiter:junit-jupiter`
- Add JUnit vintage engine initially: `org.junit.vintage:junit-vintage-engine` (allows JUnit 4 tests to run during transition)
- After test migration complete: remove vintage engine dependency
- Remove explicit `junit:junit:4.x` if present (Boot 2.7 parent manages JUnit 5)
- JUnit migration strategy: see Testing Strategy During Migration section. If vintage engine incompatible with Boot 2.7, migrate tests before Boot upgrade.
- Verify: Mockito version jump (1.x → 4.x via Boot 2.7 parent). Remove unused stubs or test failures will occur from strict stubbing default.

**Phase 2 (→ Boot 3.4.5):**
- Parent version: `2.7.18` → `3.4.5`
- Java version: `<java.version>1.8</java.version>` → `<java.version>25</java.version>`
- Remove: `springfox-swagger2`, `springfox-swagger-ui`
- Add: `org.springdoc:springdoc-openapi-starter-webmvc-ui` — verify latest compatible version with Boot 3.4.5 at implementation time (2.8.0 is placeholder, check Maven Central)
- Remove: explicit `org.hibernate:hibernate-validator`
- Add: `org.springframework.boot:spring-boot-starter-validation` (provides jakarta.validation via Boot-managed Hibernate Validator)
- Remove: `spring.mvc.pathmatch.matching-strategy=ant_path_matcher` from application.properties (no longer needed without Springfox)

**Phase 3 (→ Containerization):**
- Remove: `<packaging>war</packaging>` line (defaults to jar)
- Remove: `maven-war-plugin` plugin entirely
- Remove: entire `spring-boot-starter-tomcat` dependency block (the one with `<scope>provided</scope>`) — embedded Tomcat comes transitively via `spring-boot-starter-web` and will be packaged in the JAR automatically
- Update (not add): existing `spring-boot-maven-plugin` — add `<image><name>library-api:${project.version}</name></image>` configuration block for Paketo
- Keep: `<finalName>library-api</finalName>` (produces library-api.jar, consistent with library-api:1.0.0 image name)
- Note: Paketo requires Docker-compatible daemon. For Podman: `export DOCKER_HOST=unix:///run/podman/podman.sock`

### What NOT to Change in pom.xml

- **Do not add explicit versions** to dependencies managed by Boot parent (H2, Spring Data, Hibernate) — let the parent BOM manage versions
- **Do not add** `spring-boot-starter-tomcat` back with `compile` scope — it comes transitively via `spring-boot-starter-web`
- **Do not change** `<groupId>hu.example</groupId>` or `<artifactId>library-api</artifactId>` — the project identity stays the same through migration
- **Do not add** `maven-jar-plugin` explicitly in Phase 3 — Spring Boot Maven plugin handles JAR packaging

### Decision Point: Springfox Compatibility in Phase 1

**Problem:** Springfox 2.9.2 is incompatible with Spring Boot 2.6+ due to PathPatternParser changes. Throws NullPointerException on startup.

**Option A — Temporary workaround (recommended for demo):**
Add `spring.mvc.pathmatch.matching-strategy=ant_path_matcher` to application.properties in Phase 1. Remove this property in Phase 2 when Springfox is replaced by SpringDoc. This is technically a workaround (violates NFR2 spirit), but it's explicitly temporary with a documented removal plan.

**Option B — Move SpringDoc migration to Phase 1:**
Replace Springfox with SpringDoc in Phase 1 instead of Phase 2. Eliminates the compatibility issue but mixes framework API changes with API documentation changes, making Phase 1 less focused.

**Recommendation:** Option A — the workaround is 1 line, explicitly temporary, and keeps Phase 1 focused on framework API changes. Document the `ant_path_matcher` addition as "temporary — removed in Phase 2" in the commit message.

### Testing Strategy During Migration

**Phase 1 — Test migration first:**
1. Add JUnit 5 dependencies (jupiter + vintage engine)
2. Run original tests with vintage engine — must pass (validates vintage compatibility)
3. Migrate test annotations (@RunWith → @ExtendWith, imports)
4. Remove vintage engine dependency
5. Run migrated tests — must pass (validates test migration)
6. THEN proceed with Phase 1 code changes (findOne, context-path, etc.)
7. Run tests after each code change — failures are code issues, not test issues

**Phase 1 Risk — Vintage Engine + SpringRunner compatibility:**
If JUnit vintage engine + @RunWith(SpringRunner.class) fails on Boot 2.7, migrate ALL test annotations to JUnit 5 BEFORE upgrading Boot parent (still on 1.5). This eliminates the vintage engine dependency entirely.

**Phase 1 Risk — Mockito strict stubbing:**
Mockito 4.x (via Boot 2.7) enables strict stubbing by default. Tests with unused `when().thenReturn()` stubs will fail. Fix: remove unused stubs (preferred) or add `@MockitoSettings(strictness = Strictness.LENIENT)` — but prefer removing unused stubs per NFR2.

**Phase 1 — Jackson response check:**
Response spot-check in Phase 1 (not just Phase 2): compare at least 3 endpoint JSON responses (GET /api/books, GET /api/books/1, GET /api/loans/active) between Boot 1.5 and Boot 2.7 versions. If date formatting differs, add explicit Jackson configuration immediately — don't defer to Phase 2.

**Phase 2 — Tests validate namespace changes:**
- After javax→jakarta migration, test compilation is the first validation
- Tests that import javax.persistence in assertions must also be updated
- Response parity: manually compare 3+ endpoint responses (not automated in MVP)

**Phase 3 — Container tests:**
- Same test suite runs against containerized app via exposed port
- Validates containerization didn't change behavior

### Namespace Purity Verification Command

After Phase 2 javax→jakarta migration, run:
```bash
# Must return 0 results (excluding JDK-owned packages):
grep -r "import javax\." src/main/java/ | grep -v "javax.crypto" | grep -v "javax.net" | grep -v "javax.security"
```
Any match indicates incomplete migration. Also verify no javax references in `pom.xml` dependencies (except JDK modules).

## Core Architectural Decisions

### Decision Foundation

All 8 Architecture Decision Records (ADR-001 through ADR-008) are defined in the PRD and expanded in this document's Project Context Analysis and Technology Foundation sections. This section documents the REMAINING decisions not covered by ADRs. Deferred decisions (production database, authentication, CI/CD, GraalVM native-image) are documented in PRD Post-MVP Features — not repeated here.

### Data Architecture

**Data Initialization Decision:** Keep `data.sql` with `spring.jpa.defer-datasource-initialization=true` (added Phase 1). Rationale: Spring Boot documented solution, 1 property addition, cleaner diff than renaming to `import.sql`.

Database (H2 in-memory) and DDL strategy (`ddl-auto=update`) unchanged — see project documentation for details.

### API Error Handling Architecture

Centralized via `@RestControllerAdvice` (Phase 1), replacing duplicated HashMap try/catch blocks:

| Exception | HTTP Status | When |
|-----------|------------|------|
| `IllegalArgumentException` | 404 NOT_FOUND | Entity not found |
| `IllegalStateException` | 400 BAD_REQUEST | Business rule violation |
| `MethodArgumentNotValidException` | 400 BAD_REQUEST | Bean validation failure (@Valid) |
| `Exception` (generic fallback) | 500 INTERNAL_SERVER_ERROR | Unexpected errors — prevents stack trace leakage |

Note: Spring's default handling for HTTP-level errors (405 Method Not Allowed, missing params, malformed JSON) is adequate and not overridden.

Note: `IllegalArgumentException` → 404 follows the existing codebase convention where it signals "entity not found." Custom `ResourceNotFoundException` would be cleaner REST semantics but is deferred to post-MVP (migration-not-redesign principle).

All handlers return consistent JSON format:
```json
{
  "error": "Human-readable error message",
  "status": 400
}
```

**Validation error format guidance (implementation detail in stories):**
Concatenate field errors: `"title: must not be blank, isbn: must not be blank"` — preserves simple JSON format while providing actionable details.

**Unmapped URLs:** Handled by Spring's default 404 mechanism, not by @RestControllerAdvice. This is intentional — only application-specific exceptions are centralized.

### New Code vs. Migrated Code Idiom Policy

New code written during migration (e.g., @RestControllerAdvice, new helper methods) MAY use modern Java idioms (streams, lambdas) from Phase 1 — even though existing code is only modernized in Phase 2. Rationale: forcing pre-Java 8 patterns in brand-new code is artificial. The modernization rule applies to EXISTING code migration, not new additions.

### Demo Runtime

**SDKMAN JDK switching (Option A):**
- Terminal 1: `sdk use java 8.0.482-tem && mvn spring-boot:run` (original, port 8081)
- Terminal 2: `sdk use java 25-tem && mvn spring-boot:run -Dserver.port=8082` (migrated, port override via command line — application.properties stays 8081)
- Important: Use `sdk use` (session-scoped) NOT `sdk default` (global)

Alternative approaches (Option B: container + local, Option C: both containers) documented in README.

Post-migration: update `.devcontainer/Dockerfile` to Java 25. Original Java 8 devcontainer preserved on `main` branch pre-merge.

### Implementation Dependency Order

**Phase 1** (sequential):
1. JUnit 5 test migration (on Boot 1.5 if vintage engine fails on 2.7)
2. Boot parent 2.7 + Springfox workaround + config changes + findOne→findById
3. Error handling centralization (@RestControllerAdvice)
→ **Gate: green build + response spot-check (3+ endpoints) + H2 data verified**

**Phase 2** (commit order matters):
4. Boot parent 3.x + Java 25
5. javax→jakarta namespace + verification grep
6. java.time migration + SpringDoc + Jackson config + code modernization (can be one or multiple commits, but namespace MUST compile before java.time changes — both touch entity files)
→ **Gate: green build + full response parity check + namespace grep clean**

**Phase 3** (sequential):
7. WAR→JAR + Paketo + container validation
→ **Gate: container response parity with JAR version**

### Cross-Component Dependencies

- Jackson config (ADR-006) must be validated in Phase 1 — Boot 2.7 may already change serialization defaults
- Springfox workaround (`ant_path_matcher`) in Phase 1 creates temporary NFR2 exception — tracked and removed in Phase 2 with SpringDoc migration
- Namespace migration (step 5) must compile before java.time changes (step 6) — both touch same entity files, simultaneous changes create debugging ambiguity
- Sequential commits in Phase 2: if namespace compiles but java.time fails, you know it's a type mapping issue. Combined commits make debugging ambiguous.

## Implementation Patterns & Consistency Rules

Most patterns are INHERITED from the existing codebase, not chosen. These rules specify what changes and what stays the same, preventing AI agents from accidentally "improving" conventions during migration.

### application.properties Changes Per Phase

**Phase 1 (Boot 2.7):**
```properties
# CHANGE: rename
server.servlet.context-path=/library
# (was: server.context-path=/library)

# ADD: fix data.sql execution order
spring.jpa.defer-datasource-initialization=true

# ADD: Springfox compatibility workaround (TEMPORARY — remove in Phase 2)
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
```

**Phase 2 (Boot 3.x):**
```properties
# REMOVE: Springfox workaround (no longer needed with SpringDoc)
# spring.mvc.pathmatch.matching-strategy=ant_path_matcher

# ADD: SpringDoc metadata (replaces SwaggerConfig.java)
springdoc.info.title=Library API v2 — Java 25 / Spring Boot 3.x
springdoc.info.description=Migrated from Java 8 / Spring Boot 1.5 via BMAD methodology

# VERIFY: Jackson config — keep if already added in Phase 1, add if not
spring.jackson.date-format=yyyy-MM-dd'T'HH:mm:ss
spring.jackson.time-zone=Europe/Budapest
```

**Phase 3:** No application.properties changes.

**PRESERVE:** All other properties (server.port, datasource, JPA, H2 console) remain unchanged. See source file for current values.

### File Changes Per Phase

- Phase 1: ADD `config/GlobalExceptionHandler.java`
- Phase 2: DELETE `config/SwaggerConfig.java` (SpringDoc auto-configures)
- Phase 3: DELETE `webapp/WEB-INF/web.xml`, MODIFY `LibraryApplication.java` (remove extends SpringBootServletInitializer)

### Migration Rules

**What AI agents MUST do:**
- Preserve ALL endpoint paths, HTTP methods, query parameters, and JSON response structure
- Run namespace purity grep after javax→jakarta changes (command in Namespace Verification section)
- Compare 3+ endpoint API responses with original before marking any phase gate as passed

**What AI agents MUST NOT do:**
- ❌ Modernize existing code outside its designated phase (lambdas/streams are Phase 2 only) — Phase 1 diff must show ONLY framework changes
- ❌ Add annotations, dependencies, or behavior that didn't exist in original (@Transactional, @Slf4j, Lombok, new deps) — these are improvements, not migration. Exception: dependencies required for compilation, documented in commit
- ❌ Change project identity (names, paths, endpoints, architecture pattern, data.sql filename)
- ❌ Combine namespace + java.time changes in one commit — sequential commits enable isolated debugging
- ❌ Override Spring's default HTTP error handling (405, missing params) — only application-specific exceptions are centralized

**Tracked Temporary Exception:**
`spring.mvc.pathmatch.matching-strategy=ant_path_matcher` added Phase 1 for Springfox compatibility. MUST be removed in Phase 2 when Springfox is replaced by SpringDoc. Epic/Story creation MUST include explicit removal story. Phase 2 gate verifies: grep for `ant_path_matcher` in application.properties returns 0 results.

### Commit Convention

Format: `[Phase X] type: description` — types: `migrate`, `config`, `test`, `refactor`
One logical change per commit. Clean git log is part of the demo repo story.

### Format Patterns

**Date serialization after java.time migration (Phase 2):**
- `LocalDateTime` → `yyyy-MM-dd'T'HH:mm:ss` (matches original) ✅
- `LocalDate` → `yyyy-MM-dd` (shorter than original `yyyy-MM-dd'T'00:00:00`) — accepted as intentional improvement, original time suffix was meaningless noise on date-only fields

**Error response format (Phase 1):**
`{"error": "Human-readable message", "status": 400}` — no additional fields (no timestamp, no path). Validation errors concatenate field messages: `"title: must not be blank, isbn: must not be blank"`

### Preserved Conventions

Project structure, naming conventions, and package organization are UNCHANGED throughout migration. New code (e.g., GlobalExceptionHandler) follows existing patterns visible in the codebase: PascalCase classes, camelCase methods. No new packages, no reorganization, no abstraction layers.

## Project Structure & Boundaries

### Project Directory Structure (Post-Migration)

```
bmad_presentation/
├── pom.xml                                    # Maven — Boot 3.4.5, Java 25, JAR packaging
├── src/
│   ├── main/
│   │   ├── java/hu/example/library/
│   │   │   ├── LibraryApplication.java        # Entry point (no longer extends ServletInitializer)
│   │   │   ├── config/
│   │   │   │   └── GlobalExceptionHandler.java # NEW: @RestControllerAdvice (Phase 1)
│   │   │   ├── model/
│   │   │   │   ├── Book.java                  # jakarta.persistence, LocalDate/LocalDateTime
│   │   │   │   ├── Member.java                # jakarta.validation, LocalDate
│   │   │   │   └── Loan.java                  # jakarta.persistence, LocalDate
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java         # findById (not findOne)
│   │   │   │   ├── MemberRepository.java
│   │   │   │   └── LoanRepository.java
│   │   │   ├── service/
│   │   │   │   ├── BookService.java           # Lambdas, streams
│   │   │   │   └── LoanService.java           # Stream API, LocalDate arithmetic
│   │   │   └── controller/
│   │   │       ├── BookController.java        # No try-catch (delegates to GlobalExceptionHandler)
│   │   │       └── LoanController.java        # No try-catch (delegates to GlobalExceptionHandler)
│   │   └── resources/
│   │       ├── application.properties         # Boot 3.x config, SpringDoc, Jackson
│   │       └── data.sql                       # Seed data (unchanged)
│   └── test/java/hu/example/library/
│       ├── service/
│       │   └── BookServiceTest.java           # JUnit 5 + Mockito 5
│       └── controller/
│           └── BookControllerTest.java        # JUnit 5 + @ExtendWith
├── .devcontainer/                             # Updated to Java 25
│   ├── Dockerfile
│   └── devcontainer.json
├── _bmad-output/planning-artifacts/           # BMAD artifacts (demo content)
│   ├── product-brief-library-api-migration.md
│   ├── product-brief-library-api-migration-distillate.md
│   ├── prd.md
│   └── architecture.md
├── docs/                                      # Project documentation
│   ├── index.md
│   ├── project-overview.md
│   ├── architecture.md
│   └── ...
└── target/                                    # Build output (library-api.jar)
```

**Removed in migration:**
- ~~`config/SwaggerConfig.java`~~ (Phase 2 — SpringDoc auto-configures)
- ~~`src/main/webapp/WEB-INF/web.xml`~~ (Phase 3 — no WAR descriptor needed)

### Architectural Boundaries

**Layer Boundaries (PRESERVED):**
```
HTTP Request → Controller → Service → Repository → JPA/H2
                  ↓ (exceptions)
         GlobalExceptionHandler → HTTP Error Response
```

- Controllers: HTTP concerns only (request mapping, response wrapping). No business logic, no try-catch.
- Services: Business logic and validation. Throw exceptions for error cases.
- Repositories: Data access only. Spring Data generated implementations.
- GlobalExceptionHandler: Cross-cutting error handling. Catches exceptions from any layer.

**Data Boundaries:**
- All data access through Spring Data JPA repositories — no direct JDBC or SQL in services
- H2 in-memory — no persistent state between restarts
- Seed data via `data.sql` — loaded after DDL via `defer-datasource-initialization`

### FR → File Mapping

| FR Category | Primary Files |
|-------------|--------------|
| FR1-FR5 (Migration Execution) | `pom.xml`, `application.properties` |
| FR6 (Namespace) | All files in `model/`, `controller/`, `config/` |
| FR7 (findOne→findById) | All files in `service/` |
| FR8 (Code modernization) | `BookService.java`, `LoanService.java`, controllers |
| FR9 (Date/time) | `Book.java`, `Member.java`, `Loan.java`, `LoanService.java` |
| FR10-FR12 (Error handling) | NEW: `GlobalExceptionHandler.java`, MODIFY: `LoanController.java` |
| FR13-FR14 (Test framework) | `BookServiceTest.java`, `BookControllerTest.java` |
| FR15-FR16 (API docs) | DELETE: `SwaggerConfig.java`, ADD: springdoc in `pom.xml` |
| FR17-FR20 (Data & config) | `application.properties`, `data.sql` |
| FR21 (Containerization) | `pom.xml`, DELETE: `web.xml`, MODIFY: `LibraryApplication.java` |

## Architecture Validation

### Architecture Completeness Checklist

**✅ Requirements Analysis**
- [x] Project context analyzed (brownfield, 14 files, 3 entities)
- [x] Scale and complexity assessed (low)
- [x] Technical constraints identified
- [x] Cross-cutting concerns mapped (response parity, namespace, date/time, phase isolation)

**✅ Architectural Decisions**
- [x] 8 ADRs documented with rationale and consequences
- [x] Technology stack with pinned versions per phase
- [x] Springfox compatibility decision resolved
- [x] Testing strategy with vintage engine fallback
- [x] New code vs. migrated code idiom policy defined

**✅ Implementation Patterns**
- [x] Migration rules (MUST 3 / MUST NOT 5) with rationale
- [x] application.properties changes per phase
- [x] pom.xml changes per phase
- [x] Commit convention defined
- [x] Date serialization format decided
- [x] Error response format specified
- [x] Namespace purity verification command defined

**✅ Project Structure**
- [x] Post-migration directory tree
- [x] Layer boundaries documented
- [x] FR → File mapping complete
- [x] File changes per phase listed

**✅ Validation & Handoff**
- [x] Critical claims verified against source code (Book.java publishedDate=Date ✓, LoanController HashMap errors ✓, pom.xml Boot 1.5.22 ✓, application.properties context-path old format ✓)
- [x] AI Agent Context Guidance defined (per-phase doc slicing)
- [x] Tracked temporary exceptions documented with removal plan (ant_path_matcher)
- [x] Navigation guide included (How to Read This Document)

### Readiness Assessment

**Status:** READY FOR IMPLEMENTATION | **Confidence:** High

High = all critical decisions made, no unresolved blockers, implementation can start immediately. One tracked temporary exception (Springfox ant_path_matcher) with explicit Phase 2 removal plan.

Confidence scale: High (start immediately) → Medium (1-2 open questions, mid-implementation correction possible) → Low (significant gaps, blockers within first phase).

This architecture is complete for this project — a 14-file demo migration. Enterprise-scale migrations would face additional edge cases not covered here.

**Note:** "Requirements coverage" means every FR has architectural support (decisions, patterns, file mapping). Implementation details (acceptance criteria, test expectations, edge cases) are defined in Epic/Story creation — the next BMAD workflow step.

### AI Agent Context Guidance

When providing this doc to an AI agent for story implementation, extract ONLY relevant sections:
- **Phase 1 stories:** Project Context + Technology Foundation (Phase 1) + Testing Strategy + Migration Rules + Core Decisions (error handling) (~100 lines)
- **Phase 2 stories:** Technology Foundation (Phase 2) + ADR-003 + Migration Rules + Namespace Verification (~100 lines)
- **Phase 3 stories:** ADR-008 + Technology Foundation (Phase 3) + Demo Runtime (~50 lines)

**First Implementation Priority:**
Phase 1, Step 1: JUnit 5 test migration. Attempt on Boot 2.7 first (vintage engine); if vintage engine + SpringRunner incompatible, fall back to migrating tests on Boot 1.5 parent before upgrading.
