# BMAD Artifact Errata

This file documents corrections discovered AFTER the BMAD artifacts were generated.
Kept separate so the original artifacts remain immutable as historical record.

## ERR-001 — Wrong Spring Boot port override syntax

**Affected:**
- `architecture.md` line 263
- `epics.md` lines 83, 494
- (Original `README.md` — already corrected)

**Wrong (as documented):**
```bash
mvn spring-boot:run -Dserver.port=8082
```

**Why it fails:**
The `-D` flag passes a system property to the **Maven JVM**, not to the Spring Boot
application's JVM. The Spring Boot Maven plugin runs the app in a forked process and
does not propagate Maven's system properties to the application by default.

**Result:**
```
APPLICATION FAILED TO START
Web server failed to start. Port 8081 was already in use.
```

**Correct alternatives:**

```bash
# Option A: environment variable (simplest, recommended)
SERVER_PORT=8082 mvn spring-boot:run

# Option B: Spring Boot Maven plugin specific syntax
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8082

# Option C: pass as JVM argument
mvn spring-boot:run -Dspring-boot.run.jvmArguments=-Dserver.port=8082
```

**Discovered:** 2026-05-03 during demo runtime setup (Story 4.3 verification).

**Lesson for the presentation:**
This is itself a great example of what BMAD-style documentation surfaces:
the architecture doc's port override instruction was technically incorrect,
but because every step is traceable, the fix is documented in one place
and downstream artifacts (story 4.3, README) can reference this errata.
