# BMAD Quick Reference Card

## A 6 modul

| Modul | Cél | Példa parancsok |
|-------|-----|-----------------|
| **Core** | Általános funkciók (review, elicitation, party mode) | `/bmad-advanced-elicitation`, `/bmad-party-mode`, `/bmad-brainstorming` |
| **BMM** | Business-Manager-Architect-Developer workflow | `/bmad-create-prd`, `/bmad-create-architecture`, `/bmad-dev-story` |
| **CIS** | Creative & Innovation (brainstorming, design thinking, presentations) | `/bmad-cis-design-thinking`, `/bmad-cis-storytelling` |
| **WDS** | Web Design System (8-step UX process) | `/wds-1-project-brief` → `/wds-8-product-evolution` |
| **TEA** | Test Architecture (ATDD, NFR, traceability) | `/bmad-testarch-framework`, `/bmad-testarch-atdd` |
| **BMB** | Builder (custom agents, workflows, modules) | `/bmad-agent-builder`, `/bmad-module-builder` |

## BMM Core Workflow

| # | Command | Mit csinál |
|---|---------|-----------|
| 1 | `/bmad-document-project` | Meglévő kódbázis feltérképezése — architektúra, API, adatmodellek |
| 2 | `/bmad-product-brief` | Vízió, scope, sikerkritérium definíció |
| 3 | `/bmad-create-prd` | Részletes követelmények (FR/NFR), user journey-k, fázis assignment |
| 4 | `/bmad-create-architecture` | Explicit technikai döntések (ADR-ek), implementáció pattern-ek |
| 5 | `/bmad-create-epics-and-stories` | Epic-ek és story-k acceptance criteria-val |
| 6 | `/bmad-sprint-planning` | Sprint terv az epics-ből |
| 7 | `/bmad-create-story` | Kontextussal feltöltött, implementálható story file |
| 8 | `/bmad-dev-story` | Story implementációja az Amelia agenttel |
| 9 | `/bmad-sprint-status` | Sprint állapot + kockázatok |
| 10 | `/bmad-retrospective` | Epic utáni retrospektív |

## Hasznos kiegészítők

| Command | Mire jó |
|---------|---------|
| `/bmad-help` | "Most mit csináljak?" — felmért állapot alapján javasol |
| `/bmad-advanced-elicitation` | 50+ módszer (Pre-mortem, Shark Tank, First Principles, Red Team) |
| `/bmad-party-mode` | Multi-agent megbeszélés (pl. PM + Architect együtt vitáznak) |
| `/bmad-code-review` | Adversarial code review (Blind Hunter + Edge Case Hunter + Acceptance Auditor) |
| `/bmad-checkpoint-preview` | Human-in-the-loop review egy változás előtt |
| `/bmad-correct-course` | Sprint közbeni pivot — jelentős változtatás kezelése |
| `/bmad-validate-prd` | PRD validáció szabványok ellen |
| `/bmad-check-implementation-readiness` | PRD/UX/Arch/Epic teljesség ellenőrzése |

## A szereplők

| Modul | Agent | Szerepkör |
|-------|-------|-----------|
| BMM | **Mary** | Business Analyst |
| BMM | **Paige** | Tech Writer |
| BMM | **John** | Product Manager |
| BMM | **Sally** | UX Designer |
| BMM | **Winston** | Architect |
| BMM | **Amelia** | Developer |
| TEA | **Murat** | Test Architect |
| WDS | **Freya** | UX Designer |
| WDS | **Saga** | Business Analyst |
| CIS | **Carson** | Brainstorming Coach |
| CIS | **Maya** | Design Thinking |
| CIS | **Dr. Quinn** | Creative Problem Solver |
| CIS | **Victor** | Innovation Strategist |
| CIS | **Sophia** | Storyteller |
| CIS | **Caravaggio** | Presentation Master |

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
| 2 | **Springfox + Boot 2.6+** — PathPatternParser breaks Springfox | Swagger UI fails on startup | Architecture decisions |
| 3 | **data.sql execution order** — Boot 2.5+ runs data.sql BEFORE DDL | App fails to start, no seed data | Architecture impl notes |
| 4 | **Jackson nanosecond precision** — LocalDateTime serializes with nanosec | Response parity breaks silently | Architecture format patterns |
| 5 | **Mockito strict stubbing** — Boot 2.7+ upgrades Mockito | Tests break for wrong reason | Architecture testing strategy |

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
