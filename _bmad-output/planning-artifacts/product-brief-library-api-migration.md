---
title: "Product Brief: Library API — Java 8 to Java 25 LTS Migration"
status: "complete"
created: "2026-04-06"
updated: "2026-04-06"
inputs:
  - docs/index.md
  - docs/architecture.md
  - docs/api-contracts.md
  - docs/data-models.md
  - docs/development-guide.md
  - web-research (Java migration landscape, Spring Boot 3.x, Paketo buildpacks)
---

# Product Brief: Library API — Java 8 to Java 25 LTS Migration

## Executive Summary

The Library API is a book lending management REST service running on Java 8 and Spring Boot 1.5 — a technology stack that reached end-of-life years ago and faces a hard security deadline in November 2026 when OpenJDK 8 public updates cease entirely.

This project modernizes the application through a phased migration path (Spring Boot 1.5 → 2.7 → 3.x) to Java 25 LTS, transitioning from traditional WAR/Tomcat deployment to cloud-native containerization via Paketo buildpacks. The migration demonstrates how the BMAD (Business-Manager-Architect-Developer) methodology brings structure and AI-assisted planning to brownfield modernization — transforming what is typically an ad-hoc, error-prone process into a disciplined, traceable workflow.

The goal: show developers how BMAD works in practice — from initial project analysis through structured planning to working, migrated code.

## The Problem

**Nearly 51% of Java developers are still running on legacy versions (8, 11, or older).** These teams face:

- **Security exposure** — Java 8 public updates end November 2026. After that, unpatched CVEs accumulate.
- **Framework rot** — Spring Boot 1.5 has been unsupported since August 2019. No security patches, no bug fixes, no community help.
- **Developer friction** — Pre-lambda code idioms, java.util.Date, anonymous inner classes. Modern developers find this code hard to maintain and unattractive to work on.
- **Deployment debt** — WAR files on Tomcat servers require manual server management, lack container orchestration benefits, and miss modern CI/CD patterns.
- **Hiring risk** — Teams on legacy stacks struggle to attract and retain talent.

The current Library API embodies all of these: `javax.*` annotations, `SimpleDateFormat`, manual for-loops instead of streams, JUnit 4, and a WAR deployment model that predates the container era.

## The Solution

Modernize the Library API through a structured, AI-assisted migration:

**Technology Migration:**
- Java 1.8 → Java 25 LTS (latest Long-Term Support)
- Spring Boot 1.5.22 → Spring Boot 3.x (latest stable)
- `javax.*` → `jakarta.*` namespace (mandatory for Spring Boot 3.x)
- `java.util.Date` / `Calendar` → `java.time.*` API
- JUnit 4 → JUnit 5 (Jupiter)
- Springfox Swagger 2.x → SpringDoc OpenAPI 3
- WAR/Tomcat → Executable JAR with Paketo buildpack containerization

**Code Modernization:**
- Anonymous Comparators → Lambda expressions
- Manual for-loops → Stream API
- `findOne()` → `findById().orElse()`
- `server.context-path` → `server.servlet.context-path`

**Methodology Demonstration:**
- BMAD workflow applied end-to-end: Document Project → Product Brief → PRD → Architecture → Epics & Stories
- Each phase produces traceable artifacts that inform the next
- AI-assisted planning reduces guesswork and catches migration pitfalls early

## What Makes This Different

This is not just a migration — it is a **methodology showcase**:

1. **Structured over ad-hoc** — BMAD breaks the migration into discoverable, reviewable artifacts instead of "just start changing imports and pray"
2. **AI-assisted planning** — Claude Code acts as domain-aware collaborator, identifying dependencies, and generating comprehensive task breakdowns
3. **Catches what humans miss** — BMAD-driven analysis surfaces critical migration landmines like the javax.crypto trap: `javax.persistence.*` must migrate to `jakarta.*`, but JDK-owned packages (`javax.crypto`, `javax.net.ssl`) must NEVER be renamed. A naive find-and-replace ships broken cryptography. BMAD catches this at the planning stage, not in production
4. **End-to-end traceability** — From product brief to individual stories, every decision is documented and every task traces back to a requirement
5. **Reproducible pattern** — The same BMAD workflow applies to any brownfield modernization: different languages, different frameworks, same structured approach

## Who This Serves

**Developers and development teams** facing Java/Spring Boot migration deadlines. They know they need to migrate but lack a structured approach. They've seen estimates range from "a few days" to "months" for similar projects and want predictability. This demo shows them exactly how BMAD breaks a migration into manageable, traceable steps — from initial codebase analysis to working stories they can pick up and implement.

## Success Criteria

| Metric | Target |
|--------|--------|
| Application compiles and runs on Java 25 | Yes |
| All 12 REST endpoints functional | Verified via automated tests + Swagger UI |
| All existing tests pass on JUnit 5 | Green build |
| Containerized via Paketo buildpack | OCI image builds and runs |
| Zero `javax.*` references (except JDK-owned) | Clean namespace |
| Modern code idioms adopted | Lambdas, streams, java.time |
| BMAD artifacts complete | Brief, PRD, Architecture, Epics |
| Presentation-ready | Reveal.js deck with live demo points |

## Scope

**In scope (v1):**
- Phased migration: Spring Boot 1.5 → 2.7 → 3.x (following Spring's recommended path, each phase as a separate epic with clear milestones)
- All namespace, API, and code idiom updates
- Paketo buildpack containerization
- Test framework migration (JUnit 4 → 5)
- API documentation migration (Springfox → SpringDoc)
- Complete BMAD artifact set
- Reveal.js presentation for developer audience

**Out of scope:**
- Production database migration (H2 in-memory is sufficient for demo)
- Authentication/authorization implementation
- GraalVM native-image compilation (natural Phase 2 after core migration — dramatically reduces startup time and memory, but adds significant build complexity)
- Performance benchmarking
- CI/CD pipeline setup
- Multi-environment configuration

## Vision

If this migration demo succeeds, it becomes a **reference implementation** for BMAD-driven brownfield modernization:

- Template for Java migration workshops and training
- Reusable BMAD workflow patterns for other technology migrations (.NET, Node.js, Python)
- Evidence that AI-assisted planning is not theoretical — it produces working code and traceable decisions
- Foundation for a broader "BMAD Migration Playbook" covering common enterprise modernization scenarios
- **Living documentation** — every BMAD artifact (PRD, architecture doc, epics) doubles as onboarding material and decision audit trail for the modernized codebase. The next developer who touches this code has documented rationale for every structural decision — solving the institutional knowledge gap that plagues legacy systems
