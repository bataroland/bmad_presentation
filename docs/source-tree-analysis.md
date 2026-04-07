# Source Tree Analysis — Library API

> Generated: 2026-04-06 | Scan Level: Quick

## Annotated Directory Tree

```
bmad_presentation/
├── pom.xml                          # Maven build — Spring Boot 1.5.22, WAR, Java 8
├── src/
│   ├── main/
│   │   ├── java/hu/example/library/
│   │   │   ├── LibraryApplication.java        # ★ Entry point — SpringBootServletInitializer
│   │   │   ├── config/
│   │   │   │   └── SwaggerConfig.java         # Springfox Swagger 2 configuration
│   │   │   ├── model/                         # JPA entities (javax.persistence.*)
│   │   │   │   ├── Book.java                  # Book entity — java.util.Date, SimpleDateFormat
│   │   │   │   ├── Member.java                # Member entity — javax.validation, @Temporal
│   │   │   │   └── Loan.java                  # Loan entity — Calendar-based dates, @ManyToOne
│   │   │   ├── repository/                    # Spring Data JPA repositories
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── MemberRepository.java
│   │   │   │   └── LoanRepository.java
│   │   │   ├── service/                       # Business logic layer
│   │   │   │   ├── BookService.java           # Anonymous Comparator (pre-lambda style)
│   │   │   │   └── LoanService.java           # Manual for-loop filtering (no streams)
│   │   │   └── controller/                    # REST API endpoints
│   │   │       ├── BookController.java        # CRUD + search + availability (7 endpoints)
│   │   │       └── LoanController.java        # Borrow/return + queries (5 endpoints)
│   │   ├── resources/
│   │   │   ├── application.properties         # Server config — server.context-path (old format)
│   │   │   └── data.sql                       # Seed data for H2
│   │   └── webapp/WEB-INF/
│   │       └── web.xml                        # Traditional WAR deployment descriptor
│   └── test/java/hu/example/library/
│       ├── service/
│       │   └── BookServiceTest.java           # JUnit 4 + Mockito unit tests
│       └── controller/
│           └── BookControllerTest.java        # JUnit 4 controller tests
├── .devcontainer/                             # Podman-based dev container
│   ├── Dockerfile                             # Java 8 (Temurin) + Tomcat 9 + Maven
│   └── devcontainer.json                      # VS Code + Claude Code integration
└── target/                                    # Maven build output (compiled classes, WAR)
```

## Critical Folders

| Folder | Purpose | File Count |
|--------|---------|------------|
| `model/` | JPA entities with javax.persistence annotations | 3 |
| `repository/` | Spring Data JPA repositories | 3 |
| `service/` | Business logic (pre-Java 8 idioms) | 2 |
| `controller/` | REST API endpoints (Spring MVC) | 2 |
| `config/` | Application configuration (Swagger) | 1 |
| `resources/` | Properties and seed data | 2 |
| `webapp/WEB-INF/` | WAR deployment descriptor | 1 |

## Entry Points

- **Application bootstrap:** `LibraryApplication.java` — extends `SpringBootServletInitializer` for WAR deployment
- **Embedded run:** `mvn spring-boot:run` (port 8081)
- **External Tomcat:** Deploy `library-api.war` to Tomcat 9
