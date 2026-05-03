# Library API — BMAD Migration Demo

A Java 8 / Spring Boot 1.5 REST API migrated to Java 25 / Spring Boot 3.x using the **BMAD (Business-Manager-Architect-Developer)** methodology.

This repository demonstrates how structured, AI-assisted planning transforms ad-hoc migration into a traceable, predictable process.

## Quick Start

```bash
git clone https://github.com/bataroland/bmad_presentation.git
cd bmad_presentation
mvn spring-boot:run
```

App runs at: http://localhost:8081/library/swagger-ui/index.html

## What's Inside

### The Application

A book lending management REST API:
- 3 entities (Book, Member, Loan)
- 12 REST endpoints (CRUD + borrowing)
- H2 in-memory database with seed data

### The Migration (Java 8 → Java 25)

| Phase | Branch | What Changed |
|-------|--------|-------------|
| Original | `main` (initial commit `b93e61e`) | Java 8, Boot 1.5, WAR, javax.*, JUnit 4 |
| Phase 1 | `phase-1/boot-2.7` | Boot 2.7, JUnit 5, findById, @RestControllerAdvice |
| Phase 2 | `phase-2/boot-3.x` | Boot 3.x, Java 25, jakarta.*, java.time, streams |
| Phase 3 | `phase-3/containerize` | JAR packaging, Paketo buildpack, Docker-in-Docker |

### The BMAD Artifacts

All planning documents are in `_bmad-output/planning-artifacts/`:

| # | Read This | What You Learn |
|---|-----------|---------------|
| 1 | `product-brief-library-api-migration.md` | Why we migrated — the business case and vision |
| 2 | `prd.md` | What we committed to — 34 requirements, success criteria, user journeys |
| 3 | `architecture.md` | How we decided — 8 ADRs, phase assignment, migration patterns |
| 4 | `epics.md` | What we implemented — 4 epics, 21 stories with acceptance criteria |

The `product-brief-library-api-migration-distillate.md` contains dense context for downstream AI consumption.

## Viewing the Presentation

Open `presentation/index.html` in any browser. No server needed.

- Arrow keys or Space to navigate
- Press `S` for speaker notes
- Press `F` for fullscreen

## Running Both Versions Side-by-Side

```bash
# Terminal 1 — Original (Java 8)
sdk use java 8.0.482-tem
git checkout b93e61e
mvn spring-boot:run
# → localhost:8081/library/swagger-ui.html

# Terminal 2 — Migrated (Java 25)
sdk use java 25.0.2-tem
git checkout main
SERVER_PORT=8082 mvn spring-boot:run
# → localhost:8082/library/swagger-ui/index.html
```

## Building the Container Image

```bash
# Start Docker daemon (inside devcontainer)
dockerd --storage-driver vfs > /tmp/dockerd.log 2>&1 &
sleep 5

# Build with Paketo
mvn spring-boot:build-image -DskipTests

# Run
docker run -p 8082:8081 library-api:1.0.0
```

## Troubleshooting

### Java version mismatch
```bash
sdk list java              # See installed versions
sdk use java 25.0.2-tem    # Switch to Java 25
java -version              # Verify
```

### Paketo build fails — "permission denied" on socket
The devcontainer needs `--privileged` in `runArgs` and Docker daemon running inside:
```bash
dockerd --storage-driver vfs > /tmp/dockerd.log 2>&1 &
```

### Paketo build fails — API version too low
Ensure Docker 20.10+ is installed (API 1.41 required by Spring Boot 3.x).

### Maven dependency issues
```bash
mvn dependency:resolve     # Download all dependencies
mvn clean test             # Verify build
```

### H2 Console access
URL: http://localhost:8081/library/h2-console
- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa`
- Password: (empty)

## Project Structure

```
bmad_presentation/
├── pom.xml                          # Maven — Boot 3.5.13, Java 25, JAR
├── src/main/java/hu/example/library/
│   ├── LibraryApplication.java      # Entry point
│   ├── config/GlobalExceptionHandler.java  # Centralized error handling
│   ├── model/                       # JPA entities (jakarta.persistence)
│   ├── repository/                  # Spring Data JPA
│   ├── service/                     # Business logic (streams, java.time)
│   └── controller/                  # REST endpoints
├── src/test/java/                   # JUnit 5 tests
├── _bmad-output/planning-artifacts/ # BMAD artifacts
├── docs/                            # Project documentation
├── presentation/                    # Reveal.js slides
├── BMAD-CHEAT-SHEET.md             # Quick reference card
└── .devcontainer/                   # Java 25 + Docker-in-Docker
```

## License

Demo project for BMAD methodology presentation.
