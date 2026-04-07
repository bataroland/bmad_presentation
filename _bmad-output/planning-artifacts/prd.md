---
stepsCompleted:
  - step-01-init
  - step-02-discovery
  - step-02b-vision
  - step-02c-executive-summary
  - step-03-success
  - step-04-journeys
  - step-05-domain-skipped
  - step-06-innovation-skipped
  - step-07-project-type
  - step-08-scoping
  - step-09-functional
  - step-10-nonfunctional
  - step-11-polish
  - step-12-complete
classification:
  projectType: api_backend
  domain: general
  complexity: low
  projectContext: brownfield
inputDocuments:
  - _bmad-output/planning-artifacts/product-brief-library-api-migration.md
  - _bmad-output/planning-artifacts/product-brief-library-api-migration-distillate.md
  - docs/index.md
  - docs/project-overview.md
  - docs/architecture.md
  - docs/api-contracts.md
  - docs/data-models.md
  - docs/development-guide.md
documentCounts:
  briefs: 2
  research: 0
  brainstorming: 0
  projectDocs: 6
workflowType: 'prd'
---

# Product Requirements Document - Library API Migration

**Author:** Roland
**Date:** 2026-04-06

## Executive Summary

The Library API is a Java 8 / Spring Boot 1.5 book lending management REST API facing mandatory modernization as public security updates for OpenJDK 8 cease in November 2026, and Spring Boot 1.5 has been unsupported since August 2019. This PRD defines a phased migration to Java 25 LTS / Spring Boot 3.x with Paketo buildpack containerization.

BMAD (Business-Manager-Architect-Developer) is a structured workflow that uses AI-assisted agents to produce planning artifacts — product briefs, PRDs, architecture decisions, and implementation stories — before writing migration code.

The migration follows a three-phase approach — Spring Boot 1.5 → 2.7 → 3.x — following Spring's recommended migration path. For this demo, the phased approach also provides distinct milestones that make each phase's challenges visible to the audience. Each phase is gated by a green build and passing test suite before proceeding. Phase 1 addresses framework API changes (Spring Data repository API removal, configuration property renames, circular dependency handling). Phase 2 tackles the javax-to-jakarta namespace migration and Hibernate 5→6 upgrade, navigating landmines like the javax.crypto trap and Jackson serialization default changes. Phase 3 converts the WAR/Tomcat deployment model to cloud-native containerization via Paketo buildpacks, eliminating Dockerfile maintenance and enabling automatic JVM optimization and security patching — validated by running the full test suite against the containerized application and comparing API response parity with the WAR deployment.

Beyond the technical migration, this project serves as a live demonstration of the BMAD methodology applied to brownfield modernization — showing developers how structured planning produces a concrete migration plan with identified risks per phase — before you touch pom.xml. BMAD does not replace automated refactoring tools like OpenRewrite — it structures the decisions that those tools cannot make: what to migrate, in what order, and what risks to watch for at each phase.

### What Makes This Special

This is not a migration guide — it is a methodology proof. Most teams "just start" migrating and discover problems in production. BMAD front-loads the discovery: the planning phase surfaces critical pitfalls — the javax.crypto trap (JDK-owned packages must never be renamed), circular dependency rejection in Boot 2.6+, silent Jackson serialization changes, H2 dialect incompatibilities — before a single line of code changes. Every BMAD artifact doubles as living documentation for the modernized codebase — the architecture doc explains why jakarta was chosen over a compatibility shim, the PRD captures which risks were accepted, and each story traces back to a specific migration requirement — accurate at migration completion, and structured to remain maintainable as the codebase evolves.

BMAD's value is not in listing known gotchas — it is in forcing explicit decisions that teams otherwise defer: should we migrate directly or in phases? Which breaking changes belong to which phase? What is the rollback strategy if Phase 2 breaks the test suite? These decisions, left implicit, become production incidents. On a 14-file demo project the methodology overhead is visible; on a 200-service enterprise estate — where a missed javax.crypto rename means a security incident, not a failing test — the same artifacts prevent the costly mistakes that no IDE refactoring catches.

## Project Classification

- **Project Type:** API Backend (REST, Spring Boot, Spring Data JPA)
- **Domain:** General (library management — demo application)
- **Complexity:** Low (14 source files, 3 entities, 12 endpoints, no auth, in-memory DB)
- **Project Context:** Brownfield (existing Java 8 application modernization)

## Success Criteria

### User Success

- Developers in the audience recognize BMAD as a practical tool they could apply to their own migration projects — not just a theoretical framework
- The before/after code comparison creates an immediate "I have this problem too" moment
- The audience leaves with a clear mental model of the BMAD workflow steps and what each produces
- The audience can estimate the time investment: BMAD planning artifacts for this project were produced in approximately [X] hours — with explicit acknowledgment of how this scales to larger codebases
- The presentation opens with audience engagement that surfaces shared migration pain — the audience recognizes their own situation before BMAD is introduced

### Business Success

- Positive audience feedback: developers express interest in trying BMAD on their own projects
- The presentation demonstrates a complete, reproducible workflow — from project analysis to running migrated application
- BMAD is perceived as complementary to existing tools (OpenRewrite, IDE refactoring), not competing with them
- The presentation includes a brief "without BMAD" contrast — showing what a best-effort manual migration typically produces (scattered notes, undocumented decisions, risks discovered in production) versus the structured BMAD output

### Technical Success

| Metric | Target |
|--------|--------|
| Application compiles and runs on Java 25 | Yes |
| All 12 REST endpoints functional | Verified via automated tests + Swagger UI + API response comparison between original and migrated versions |
| All existing tests pass on JUnit 5 | Green build |
| Containerized via Paketo buildpack | OCI image builds and runs |
| Zero `javax.*` references (except JDK-owned) | Clean namespace |
| Modern code idioms adopted | Lambdas, streams, java.time |
| BMAD artifacts complete | Brief, PRD, Architecture, Epics |
| Phase gates validated | Both 1.5→2.7 and 2.7→3.x intermediate states pass build + test suite independently |
| Reveal.js presentation quality | Before/after code comparison, at least 2 live demo points, traceability walkthrough |
| Live demo during presentation | Containerized app runs live with actual API calls, not screenshots |

### Measurable Outcomes

- Every migration phase (1.5→2.7, 2.7→3.x, containerization) produces a green build before proceeding
- Each BMAD artifact traces forward to implementation and backward to requirements
- The presentation includes at least one end-to-end traceability walkthrough: from a specific migration story back through epic, architecture decision, PRD requirement, to the original product brief problem statement
- The presentation follows a clear narrative arc: problem recognition → BMAD planning walkthrough (highlighting 2-3 key phases live, summarizing the rest with artifacts) → live migrated application → "try it yourself" call to action
- Architecture and PRD artifacts contain explicit decision records for: migration phasing strategy, per-phase risk assignments, and rollback approach

## Product Scope & Development Plan

### MVP Strategy

**Approach:** Problem-solving MVP — prove that BMAD methodology produces a working migration with traceable decisions, delivered as a developer presentation with live demo.

**Timeline:** 8 working days (deadline: 2026-04-14)
**Resource:** Solo developer (Roland) with AI-assisted planning (Claude Code + BMAD)

### MVP Feature Set

**Core User Journeys Supported:**
- Journey 1 (Roland): Full BMAD workflow → migrated app → presentation
- Journey 2 (Kata): Presentation with audience engagement, traceability walkthrough, quick reference card
- Journey 3 (Gergő): Self-contained demo repository for post-presentation exploration

**Must-Have Capabilities:**
- Phased migration completed (Boot 1.5 → 2.7 → 3.x, Java 25) with phase gates
- All 12 endpoints functional with response parity verification
- Paketo buildpack containerization working
- Test framework migrated (JUnit 4 → 5)
- API docs migrated (Springfox → SpringDoc)
- Complete BMAD artifact set (Brief, PRD, Architecture, Epics & Stories)
- Reveal.js presentation with narrative arc and at least 2 live demo points
- Interactive demo: Swagger UI working offline as fallback if live environment fails
- Quick reference card (1-page markdown)
- Self-contained demo repository with README and troubleshooting
- "Try it yourself" close with BMAD installation steps and first command

### Post-MVP Features

**Phase 2 (Post-Presentation):**
- GraalVM native-image compilation (reduced startup time and memory)
- CI/CD pipeline with automated migration validation
- Production database support (PostgreSQL/MySQL)
- Expanded test coverage (integration tests beyond unit tests)
- Before/after performance benchmarking

**Phase 3 (Community/Reuse):**
- BMAD Migration Playbook ��� reusable templates for other migrations
- Multi-language migration examples (.NET, Node.js, Python)
- Community-contributed migration recipes
- Workshop format version of the presentation (hands-on, 2-3 hours)

## User Journeys

These three journeys represent the same product consumed at three depths: live experience (Roland's presentation), guided exploration (Kata's first experiment), and independent validation (Gergő's deep review). The product must work at all three depths.

### Journey 1: Roland — The BMAD Presenter

**Persona:** Roland, experienced developer preparing a 1-hour BMAD methodology demo for fellow developers. He has a working Java 8 Library API and needs to transform it into a compelling migration story.

**Opening Scene:** Roland has a legacy Java 8 Spring Boot 1.5 application — a simple library management API. He knows the migration needs to happen, and he wants to use this as a vehicle to show his team how BMAD structures brownfield modernization. The challenge: he needs both a working migrated application AND a presentation that tells the story of how he got there.

**Rising Action:** Roland runs the BMAD workflow step by step: `/bmad-document-project` maps the existing codebase, `/bmad-product-brief` captures the migration vision, `/bmad-create-prd` defines detailed requirements and success criteria, `/bmad-create-architecture` makes explicit technical decisions (phased migration, jakarta namespace strategy, Paketo containerization), and `/bmad-create-epics-and-stories` breaks it all into implementable tasks. At each step, he sees BMAD surface risks he hadn't considered — the javax.crypto trap, circular dependency rejection in Boot 2.6+, Jackson serialization changes. At the architecture phase, BMAD surfaces a decision Roland hadn't anticipated: should the Springfox → SpringDoc migration happen in Phase 2 (with the jakarta namespace change) or Phase 3 (with containerization)? The architecture doc forces him to make this explicit rather than discovering the dependency mid-implementation. During Phase 2, Roland runs both versions side by side and compares JSON responses for key endpoints. He discovers that date formatting changed — the original returns `2008-08-01T00:00:00` while the migrated version returns `2008-08-01`. He adds explicit Jackson configuration to preserve the original format, documenting this decision in the architecture doc.

**Climax:** The migration is complete. The app runs on Java 25, containerized via Paketo. Roland opens the BMAD artifacts side by side with the migrated code and realizes: every code change traces back to a story, every story to an epic, every epic to an architecture decision, every decision to a PRD requirement. The traceability chain is real, not theoretical.

**Resolution:** Roland builds the Reveal.js presentation using the BMAD artifacts as the narrative backbone. During the live presentation, he opens the original Java 8 app in Swagger UI, walks through 2-3 BMAD phases live, shows the javax.crypto trap being caught at planning time, then switches to the migrated app running in a container. The audience asks: "Where do I start?"

**Requirements revealed:**
- Complete BMAD workflow must execute end-to-end without blocking issues
- All artifacts must be presentation-quality (clear, concise, visually scannable)
- Migration must produce a working, demonstrable application
- Reveal.js presentation must support live demo switching (original vs migrated)

### Journey 2: Kata — The Skeptical Audience Member

**Persona:** Kata, mid-level developer with 5 years of Spring Boot experience. She's been postponing a Java 11 → 21 migration at work because "we don't have time to plan it properly." She attends Roland's presentation expecting another tool pitch.

**Opening Scene:** Kata sits in the audience. Roland opens with: "Who here is still running a legacy Java version?" Her hand goes up along with half the room. She thinks: "Yeah, we know it's a problem — but every migration guide I've read is either too simplistic or too overwhelming."

**Rising Action:** Roland shows the original Java 8 app — Kata recognizes the patterns immediately: `javax.persistence`, `SimpleDateFormat`, anonymous Comparators, manual for-loops. "That looks like our codebase." Then Roland walks through the BMAD workflow: she sees how Document Project maps the codebase automatically, how the PRD captures not just what to migrate but what risks to watch for. She's intrigued when Roland shows the javax.crypto trap being caught at planning time — her team nearly made that exact mistake last quarter with a find-and-replace attempt. When Roland shows how Phase 1 isolates framework API changes from namespace migration, Kata has an "aha" moment: "So you never deal with findOne() removal and javax-to-jakarta at the same time? That's what killed our last attempt — five breaking changes hitting simultaneously with no way to tell which fix broke which thing."

**Climax:** Roland shows the traceability walkthrough: a specific story ("Migrate Book entity from javax.persistence to jakarta.persistence") traced back through the epic, architecture decision, PRD requirement, all the way to the product brief. Kata realizes: "This isn't just generating documents — it's creating a decision trail that I could show my tech lead to justify the migration timeline."

**Resolution:** After the presentation, Kata grabs the quick reference card. She's interested but cautious — "This worked on 14 files, but our project is 300 files across 4 modules." She opens the demo repository to check if the methodology seems scalable. The BMAD artifacts' structure — how they break the problem into phases and stories regardless of codebase size — starts to answer her concern. She decides to try `/bmad-document-project` on one module — expecting about 30 minutes for the quick scan. The result: a `docs/index.md` with architecture overview, API contracts, and data models. Some sections may be incomplete on a complex multi-module project — but even a partial scan gives her enough signal to judge whether the approach is worth pursuing further. If that output is accurate and useful, she'll propose running the full BMAD workflow on the migration project in next week's sprint planning. Before leaving, Kata asks Roland: "What do I actually need to install to try this?" She needs to know: Claude Code access, BMAD installation, and time commitment for a first experiment.

**Requirements revealed:**
- Presentation must open with audience pain recognition, not tool features
- Before/after code must be instantly recognizable to working developers
- The "without BMAD" contrast must be realistic (not a strawman)
- Quick reference card must be actionable enough to start Monday morning
- Traceability walkthrough must be concrete and impressive, not abstract
- The presentation must be honest about the demo project's simplicity — it proves the workflow, not the scale

### Journey 3: Gergő — The Post-Presentation Explorer

**Persona:** Gergő, senior developer and tech lead with 12 years of experience. He was impressed by Roland's presentation but remains skeptical — "demos always look good." He clones the demo repository that evening to verify the claims himself.

**Opening Scene:** Gergő opens the GitHub repository. He sees the README and immediately checks: is this actually self-contained, or will he need to chase dependencies for an hour? The README lists clear prerequisites (Java 8 for the original, Java 25 for the migrated version) and has a "Quick Start" section.

**Rising Action:** Gergő starts with the original Java 8 app — `mvn spring-boot:run`, Swagger UI opens, he hits a few endpoints. Works. Then he reads through the BMAD artifacts in order: product brief → PRD → architecture → epics. He's checking for substance: are these real decisions or generated fluff? He opens the architecture doc and finds the explicit decision record for phased migration (1.5→2.7→3.x) with rationale. He checks the PRD and finds the javax.crypto warning with specific files listed. "Okay, this is actual content, not templates." He tries `mvn spring-boot:build-image` and gets an error — no container runtime available. He checks the README, which has a troubleshooting section: "If using Podman, set DOCKER_HOST=..." He sets it up and the build succeeds. "Good — they actually tested this on something other than their own machine."

**Climax:** Gergő switches to the migrated branch/folder. He diffs the original and migrated `Book.java` — `javax.persistence` → `jakarta.persistence`, `java.util.Date` → `LocalDate`, `SimpleDateFormat` gone. He runs `mvn test` — green. He runs `mvn spring-boot:build-image` — Paketo builds the container. He runs it and hits the same endpoints. Same responses. "It actually works." Gergő doesn't just verify the code works — he reads the PRD's success criteria and notices the "phase gates validated" requirement. He thinks: "That's something I wouldn't have formalized." He checks the architecture doc's decision records: migration phasing strategy with explicit rationale, rollback approach documented. This is the substance he was looking for — decisions he'd have made informally are now reviewable artifacts. What convinces Gergő isn't any single artifact — it's the coherence between them. The architecture doc references specific PRD requirements. The epics trace to architecture decisions. The stories reference specific files from the document-project scan. A single ChatGPT prompt could generate any one of these — but it couldn't maintain this cross-document consistency. That's the BMAD difference.

**Resolution:** Gergő doesn't fire off a Slack message immediately. Instead, he adds a spike story to next sprint's backlog: "Evaluate BMAD methodology for payment-service migration — run document-project, assess artifact quality, estimate planning overhead vs. ad-hoc approach." He attaches the demo repository link and his comparison notes to the ticket. Before writing the Slack message, Gergő drafts a quick comparison: "Last time we migrated ad-hoc, it took 3 weeks and we found 2 production bugs afterward. With BMAD, the planning adds maybe a day, but the phased approach and documented risks mean fewer surprises." He includes this comparison in his message to make the case to his team.

### Journey Requirements Summary

| Capability | Revealed By | Priority |
|-----------|------------|----------|
| End-to-end BMAD workflow execution | Journey 1 (Roland) | Must have |
| Live demo switching (original vs migrated) | Journey 1 (Roland) | Must have |
| Self-contained demo repository with README | Journey 3 (Gergő) | Must have |
| API response parity verification | Journey 3 (Gergő) | Must have |
| Actionable quick reference card | Journey 2 (Kata) | Must have |
| Concrete traceability walkthrough | Journey 2 (Kata) | Must have |
| Presentation-quality BMAD artifacts | Journey 1 (Roland) | Should have |
| Audience pain recognition opening | Journey 2 (Kata) | Should have |
| Clean, reviewable code diff | Journey 3 (Gergő) | Should have |
| README with troubleshooting section | Journey 3 (Gergő) | Should have |
| BMAD artifacts withstand expert scrutiny | Journey 3 (Gergő) | Should have |
| Reproducible migration path via stories | Journey 3 (Gergő) | Should have |
| "Try it yourself" includes tooling prerequisites | Journey 2 (Kata) | Should have |
| Realistic "without BMAD" contrast | Journey 2 (Kata) | Should have |
| README includes suggested reading order for BMAD artifacts | General | Nice to have |

## API Backend Specific Requirements

### Project-Type Overview

This is a brownfield REST API backend migration. The existing Library API exposes 12 endpoints across 2 controllers (BookController, LoanController) serving JSON over HTTP. The migration preserves all existing API contracts while modernizing the underlying framework, namespace, and deployment model.

### Endpoint Specifications

All 12 existing endpoints are preserved with identical paths, HTTP methods, and request/response contracts:

**BookController** (`/api/books`): GET (list), GET (by id), POST (create), PUT (update), DELETE (delete), GET (available), GET (search)

**LoanController** (`/api/loans`): POST (borrow), POST (return), GET (active), GET (overdue), GET (by member)

**Migration impact on endpoints:**
- Context path configuration changes (`server.context-path` → `server.servlet.context-path`)
- Jackson serialization defaults may change date formatting — explicit configuration required to preserve response parity
- No path or contract changes — the API surface remains identical pre- and post-migration

**Actuator endpoints (implicit):**
Spring Boot Actuator is included transitively via spring-boot-starter. In Boot 1.5, health check is at `/health`. In Boot 2.x+, it moves to `/actuator/health`. While no explicit Actuator configuration exists in this project, the path change should be noted for Phase 1 migration — any monitoring or load balancer configured against the old path will break silently.

### Authentication Model

No authentication. The API is open by design — this is a demo application. Authentication/authorization is explicitly out of scope for this migration (see Product Scope). Adding an auth layer would be a natural post-migration enhancement but would dilute the migration story.

### Data Schemas

3 JPA entities with 2 relationships:
- **Book** (`books`) — `src/main/java/hu/example/library/model/Book.java`: id, title, author, isbn, publishedDate, createdAt, available
- **Member** (`members`) — `src/main/java/hu/example/library/model/Member.java`: id, name, email, membershipDate, address
- **Loan** (`loans`) — `src/main/java/hu/example/library/model/Loan.java`: id, book (FK → Book), member (FK → Member), borrowDate, dueDate, returnDate

**Migration impact on schemas:**
- `javax.persistence.*` → `jakarta.persistence.*` annotations on all entities
- Date/time field mapping (not all fields migrate to the same type):
  - `publishedDate` (Book) → `LocalDate` (date only, no time component)
  - `membershipDate` (Member) → `LocalDate` (date only)
  - `createdAt` (Book) → `LocalDateTime` (timestamp with time component)
  - `borrowDate`, `dueDate`, `returnDate` (Loan) → `LocalDate` (date only)
- `@Temporal` annotations removed (not needed with java.time types)
- `SimpleDateFormat` → `DateTimeFormatter` (thread-safe)
- `Calendar`-based arithmetic → `LocalDate.plusDays()` / `LocalDate.minusDays()`
- Hibernate 5.0 → 6.x may change DDL generation and SQL type mappings for H2

### Error Handling

**Current state:** Manual `HashMap<String,String>` error responses in LoanController — duplicated try/catch blocks for each endpoint.

**Migration target:** Centralized error handling via `@RestControllerAdvice` with `@ExceptionHandler` methods:
- `IllegalArgumentException` → 404 NOT_FOUND with structured error body
- `IllegalStateException` → 400 BAD_REQUEST with structured error body
- Generic exception → 500 INTERNAL_SERVER_ERROR

Error response format follows a simple JSON structure consistent with the original API behavior:
```json
{
  "error": "Human-readable error message",
  "status": 400
}
```
Note: RFC 7807 Problem Details is a natural evolution but out of scope for this migration — the goal is to centralize, not redesign.

This is both a code modernization and a structural improvement — the current approach violates DRY and makes consistent error responses fragile.

### API Documentation

**Current:** Springfox Swagger 2.9.2 with `@EnableSwagger2` and manual `Docket` bean configuration. Swagger UI at `/swagger-ui.html`.

**Migration target:** SpringDoc OpenAPI 3. No manual configuration needed — auto-detects Spring MVC endpoints. Swagger UI endpoint changes to `/swagger-ui/index.html`. OpenAPI 3.0 spec available at `/v3/api-docs`.

**Migration notes:**
- Springfox is incompatible with Spring Boot 3.x — this is a mandatory change
- SpringDoc dependency: `org.springdoc:springdoc-openapi-starter-webmvc-ui`
- Remove `SwaggerConfig.java` entirely — SpringDoc auto-configures
- API metadata (title, description, version) moves to `application.properties`

**Note:** The current controllers use no Springfox-specific annotations (`@ApiOperation`, `@ApiParam`, etc.) — only standard Spring MVC annotations. This makes the Springfox → SpringDoc migration a clean dependency swap with no controller code changes. In projects with heavy Springfox annotations, a parallel annotation migration would be needed.

### Implementation Considerations (ordered by risk)

1. **HIGH RISK — Response parity (Jackson dates + error responses):** Every endpoint must return identical JSON structure and values post-migration. Date formatting is the highest-risk area due to Jackson default changes between Boot 1.5 and 3.x. Requires explicit `spring.jackson.date-format` and `spring.jackson.time-zone` configuration, validated by comparing actual API responses between original and migrated versions. Requires validation on two fronts: (a) successful responses — Jackson date formatting, null handling; (b) error responses — the @RestControllerAdvice migration may change error behavior for edge cases (invalid types, missing params) that were previously handled by Spring's default error controller.
2. **MEDIUM RISK — H2 and data initialization:** H2 version and Hibernate 6 change DDL generation. Two specific issues: (a) Spring Boot 2.5+ changed `data.sql` execution order — it now runs BEFORE Hibernate DDL by default (was AFTER in Boot 1.5). Fix: set `spring.jpa.defer-datasource-initialization=true` or migrate to `import.sql`. (b) Hibernate 6 generates different column types for H2 (e.g., `CHARACTER VARYING` instead of `VARCHAR`). Functionally equivalent but explicit type references in data.sql may break.
3. **MEDIUM RISK — Spring Data method resolution:** Custom query methods in repositories should be validated — Spring Data 3.x has stricter method name parsing that may reject previously accepted method names.
4. **LOW RISK — Repository API migration:** All `findOne(id)` calls must be replaced with `findById(id).orElse(null)` or `.orElseThrow()` — this is a compile-time error that cannot be missed.
5. **LOW RISK — Namespace purity verification:** After javax→jakarta migration, verify no mixed namespaces remain in any file. Search for residual `javax.persistence`, `javax.validation` imports that may have been missed. Simple grep check prevents subtle compile errors.
6. **Phase 3 scope (detailed in Architecture):** WAR → JAR conversion (remove maven-war-plugin, remove provided scope from spring-boot-starter-tomcat, remove web.xml, remove SpringBootServletInitializer), Paketo buildpack configuration, and container runtime validation are Phase 3 concerns — specified in the Architecture document, not repeated here.
7. **Rollback approach:** Each phase is implemented on a separate git branch. If a phase fails its gate (build + test suite), the rollback is a branch revert — no partial migration state persists on main. Phase 3 (containerization) is additive: the JAR packaging works with or without Paketo, so rollback means simply not building the container image.

### Phase Assignment

Each technical requirement maps to a specific migration phase:

**Phase 1 (Boot 1.5 → 2.7):**
- `server.context-path` → `server.servlet.context-path`
- `findOne()` → `findById().orElse()`
- `data.sql` execution order fix (`defer-datasource-initialization`)
- Actuator path change (`/health` → `/actuator/health`)
- JUnit 4 → JUnit 5 migration
- Error handling centralization (`@RestControllerAdvice`)

**Phase 2 (Boot 2.7 → 3.x):**
- `javax.*` → `jakarta.*` namespace migration (all entities, controllers)
- Hibernate 5.6 → 6.x (column type mapping changes)
- Springfox → SpringDoc migration (remove SwaggerConfig.java)
- Jackson date formatting explicit configuration
- Namespace purity verification
- java.util.Date/Calendar → java.time.* migration
- Code modernization (lambdas, streams, diamond operator)

**Phase 3 (Containerization):**
- WAR → executable JAR conversion
- Paketo buildpack configuration
- Container runtime validation (test suite + response parity)

### Architecture Decision Records (Summary)

| ADR | Decision | Status |
|-----|----------|--------|
| ADR-001 | Phased migration (1.5→2.7→3.x) over direct jump | Accepted |
| ADR-002 | Centralize error handling with @RestControllerAdvice, preserve original response format | Accepted |
| ADR-003 | Differentiated date/time mapping (LocalDate vs LocalDateTime per field) | Accepted |
| ADR-004 | Springfox → SpringDoc (clean dependency swap, zero controller changes) | Accepted |
| ADR-005 | Git branch per phase as rollback strategy | Accepted |
| ADR-006 | Explicit Jackson configuration to guarantee response parity | Accepted |

Full ADR details will be expanded in the Architecture document.

### Risk Mitigation Strategy

**Technical Risks:**
| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| Jackson date formatting breaks response parity | High | High | Explicit `spring.jackson.date-format` config, side-by-side comparison per phase |
| H2 data.sql execution order change | Medium | Medium | `defer-datasource-initialization=true`, tested in Phase 1 |
| Hibernate 6 DDL type changes | Low | Low | Functionally equivalent, verify data.sql compatibility |
| Springfox→SpringDoc URL change breaks demo | Low | Medium | Update all references, test Swagger UI accessibility |

**Presentation Risks:**
| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| Live demo environment unavailable | Medium | High | Interactive demo: Swagger UI running locally on presenter laptop, no network dependency |
| BMAD workflow takes too long to demo live | Medium | Medium | Pre-generate all artifacts, show 2-3 key phases live, summarize the rest |
| Audience doesn't see value in BMAD | Low | High | Open with pain recognition, include "without BMAD" contrast, close with "try it yourself" |

**Timeline Risks:**
| Risk | Likelihood | Impact | Mitigation |
|------|-----------|--------|------------|
| Migration hits unexpected blocker | Medium | High | Phase gate approach — if Phase 2 blocks, demo Phase 1 result + planned Phase 2 artifacts |
| Presentation creation takes longer than expected | Medium | Medium | BMAD artifacts ARE the presentation content — Reveal.js wraps them, doesn't recreate |
| Not enough time for demo repo polish | Low | Medium | README and troubleshooting are the priority — visual polish is secondary |

### 8-Day Timeline Estimate

| Day | Focus |
|-----|-------|
| 1-2 | BMAD workflow completion (Architecture + Epics & Stories) |
| 3-4 | Phase 1 migration (Boot 1.5 → 2.7) + validation |
| 5-6 | Phase 2 migration (Boot 2.7 → 3.x) + Phase 3 containerization |
| 7 | Reveal.js presentation + quick reference card |
| 8 | Demo repo polish, README, rehearsal |

## Functional Requirements

### Migration Execution

- FR1: Developer can migrate Spring Boot version from 1.5 to 2.7 with all framework API changes applied and a green build
- FR2: Developer can migrate Spring Boot version from 2.7 to 3.x with all namespace and Hibernate changes applied and a green build
- FR3: Developer can convert WAR packaging to executable JAR and build a Paketo OCI container image
- FR4: Developer can verify each migration phase independently via build + test suite before proceeding to the next phase
- FR5: Developer can roll back a failed migration phase by reverting the corresponding git branch

### Namespace & API Modernization

- FR6: Developer can migrate all javax.* annotations to jakarta.* equivalents across entities and controllers, verified by namespace purity check (JDK-owned packages like javax.crypto excluded)
- FR7: Developer can replace all `findOne()` repository calls with `findById().orElse()` or `.orElseThrow()`

### Code Modernization

- FR8: Developer can modernize pre-Java 8 code idioms to current standards (anonymous classes → lambdas, manual loops → streams, explicit generics → diamond operator)
- FR9: Developer can migrate all date/time handling from java.util.Date/Calendar/SimpleDateFormat to java.time.* equivalents with per-field type mapping (LocalDate for date-only fields, LocalDateTime for timestamps)

### Error Handling

- FR10: Developer can centralize error handling in a `@RestControllerAdvice` class replacing duplicated try/catch blocks
- FR11: System returns structured JSON error responses (`{"error": "message", "status": code}`) for all exception types
- FR12: System preserves original error behavior for `IllegalArgumentException` (404) and `IllegalStateException` (400)

### Test Framework

- FR13: Developer can migrate all test classes from JUnit 4 to JUnit 5 (Jupiter annotations, extensions)
- FR14: All existing tests pass on the migrated codebase with identical assertions

### API Documentation

- FR15: Developer can replace Springfox Swagger 2 with SpringDoc OpenAPI 3 as the API documentation framework
- FR16: System exposes Swagger UI at `/swagger-ui/index.html` with all 12 endpoints documented

### Data & Configuration

- FR17: System initializes seed data correctly on startup regardless of Spring Boot version (data.sql executes after schema creation)
- FR18: Developer can configure explicit Jackson date format and timezone to preserve response parity
- FR19: System loads seed data from `data.sql` on startup with identical data as the original version
- FR20: All 12 REST endpoints return identical JSON response structure and values as the original version (validated independently of unit tests — test passage alone does not guarantee response parity)

### Containerization

- FR21: Containerized application runs all 12 endpoints with response parity to the JAR version

### BMAD Artifacts

- FR22: BMAD workflow produces complete artifact set (Product Brief, PRD, Architecture, Epics & Stories) with cross-document traceability, explicit decision records, and specific file/code references — not template-level output but project-specific substance
- FR23: Epics and stories are specific enough that a developer unfamiliar with the project can execute them using only the story description and the codebase — reproducible migration path

### Presentation

- FR24: Presenter can show a Reveal.js presentation covering the full migration journey with narrative arc
- FR25: Presenter can switch between original (Java 8) and migrated (Java 25) applications live during the presentation, with both running locally without network dependency
- FR26: Presenter can demonstrate at least 2 BMAD workflow phases live
- FR27: Presenter can show an end-to-end traceability walkthrough (story → epic → architecture → PRD → brief)
- FR28: Presentation follows narrative arc (pain recognition → BMAD walkthrough → live demo → "try it yourself" with tooling prerequisites) and includes time investment estimate and "without BMAD" contrast

### Demo Repository & Takeaways

- FR29: Audience member can access a one-page quick reference card with BMAD workflow steps and migration pitfalls
- FR30: Post-presentation explorer can clone the demo repository and run both original and migrated versions using only the README
- FR31: Demo repository contains all BMAD artifacts alongside the code for self-guided exploration
- FR32: Demo repository provides a clean, reviewable diff between original and migrated code (via git branches or side-by-side folders)
- FR33: Demo repository README includes troubleshooting section for common setup issues (container runtime, Java version, Maven)
- FR34: Demo repository README includes suggested reading order for BMAD artifacts with one-line explanation of what each teaches

## Non-Functional Requirements

### Migration Quality

- NFR1: Each migration phase completes with zero compiler warnings in project source code (src/main/java, src/test/java) when compiled with the target JDK (Java 25) — framework-internal deprecation warnings excluded
- NFR2: Migrated code uses no migration workarounds (@Lazy for circular dependencies, Spring compatibility flags, javax compatibility shims) — all breaking changes are resolved properly, not suppressed

### Presentation Quality

- NFR3: Reveal.js presentation loads and renders correctly without network access
- NFR4: Both original and migrated applications can run simultaneously on different ports (original: 8081, migrated: 8082 — documented in README)

### Repository Quality

- NFR5: Demo repository builds successfully with a single `mvn` command on any JDK 17+, no manual dependency setup or environment configuration beyond JDK installation
- NFR6: README quick start requires no more than 3 commands to run in embedded mode (clone, cd, mvn spring-boot:run). Container mode documented separately.
