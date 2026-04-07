# Development Guide — Library API

> Generated: 2026-04-06 | Scan Level: Quick

## Prerequisites

| Tool | Version | Notes |
|------|---------|-------|
| Java JDK | 1.8 (Temurin recommended) | Managed via SDKMAN in devcontainer |
| Maven | 3.8+ | Included in devcontainer |
| Tomcat | 9.x | For external WAR deployment (optional) |
| Podman / Docker | Latest | For devcontainer |

## Quick Start

### Using Devcontainer (Recommended)

1. Open project in VS Code / Codespaces
2. Reopen in Container (uses `.devcontainer/devcontainer.json`)
3. Dependencies are auto-resolved via `postCreateCommand`

### Manual Setup

```bash
# Verify Java 8
java -version

# Build
mvn clean compile

# Run tests
mvn test

# Run application (embedded Tomcat, port 8081)
mvn spring-boot:run
```

## Running the Application

### Embedded Mode

```bash
mvn spring-boot:run
```

- **URL:** http://localhost:8081/library/api/books
- **Swagger UI:** http://localhost:8081/library/swagger-ui.html
- **H2 Console:** http://localhost:8081/library/h2-console
  - JDBC URL: `jdbc:h2:mem:librarydb`
  - Username: `sa`
  - Password: (empty)

### WAR Deployment

```bash
mvn clean package
# Deploy target/library-api.war to Tomcat 9 webapps/
```

## Build Commands

| Command | Purpose |
|---------|---------|
| `mvn clean compile` | Compile source |
| `mvn test` | Run unit tests |
| `mvn package` | Build WAR file |
| `mvn spring-boot:run` | Run with embedded Tomcat |

## Project Configuration

- **Main config:** `src/main/resources/application.properties`
- **Seed data:** `src/main/resources/data.sql`
- **WAR descriptor:** `src/main/webapp/WEB-INF/web.xml`

## Testing

- **Framework:** JUnit 4 + Mockito
- **Run tests:** `mvn test`
- **Test sources:** `src/test/java/`
- **Test classes:**
  - `BookServiceTest` — Service layer unit tests
  - `BookControllerTest` — Controller layer tests
