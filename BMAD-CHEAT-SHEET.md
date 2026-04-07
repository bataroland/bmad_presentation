# BMAD Migration Quick Reference Card

## Workflow Steps

| # | Command | What it does |
|---|---------|-------------|
| 1 | `/bmad-document-project` | Scans existing codebase, generates architecture overview, API contracts, data models |
| 2 | `/bmad-product-brief` | Defines migration vision, scope boundaries, success criteria |
| 3 | `/bmad-create-prd` | Produces detailed requirements (FRs/NFRs), user journeys, phase assignment |
| 4 | `/bmad-create-architecture` | Documents explicit technical decisions (ADRs), implementation patterns, dependency order |
| 5 | `/bmad-create-epics-and-stories` | Breaks work into implementable stories with acceptance criteria |

## Getting Started

```bash
# 1. Install Claude Code
npm install -g @anthropic-ai/claude-code

# 2. Install BMAD
# See: github.com/bmadcode/BMAD-METHOD

# 3. Run your first scan (30 min)
/bmad-document-project
```

## Top 5 Java Migration Pitfalls (caught by BMAD)

| # | Pitfall | Impact | BMAD catches it in |
|---|---------|--------|--------------------|
| 1 | **javax.crypto trap** — JDK-owned packages must NOT be renamed to jakarta | Broken cryptography | PRD requirements |
| 2 | **Springfox + Boot 2.6+** — PathPatternParser breaks Springfox, need ant_path_matcher workaround | Swagger UI fails on startup | Architecture decisions |
| 3 | **data.sql execution order** — Boot 2.5+ runs data.sql BEFORE DDL | App fails to start, no seed data | Architecture implementation notes |
| 4 | **Jackson nanosecond precision** — LocalDateTime serializes with nanosec by default | Response parity breaks silently | Architecture format patterns |
| 5 | **Mockito strict stubbing** — Boot 2.7+ upgrades Mockito, unused stubs fail tests | Tests break for wrong reason | Architecture testing strategy |

## Phased Migration Pattern

```
Phase 1 (Boot 1.5 → 2.7): Framework API changes only
  → Gate: green build + response spot-check

Phase 2 (Boot 2.7 → 3.x): Namespace + modernization
  → Gate: green build + full response parity + namespace grep

Phase 3 (Containerize): WAR → JAR → Paketo
  → Gate: container response = JAR response
```

## Key Principle

> BMAD does not replace tools like OpenRewrite — it structures the **decisions** that tools cannot make: what to migrate, in what order, and what risks to watch for at each phase.

---

Demo repository: `github.com/[repo-link]`
BMAD Method: `github.com/bmadcode/BMAD-METHOD`
