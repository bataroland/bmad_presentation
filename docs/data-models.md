# Data Models — Library API

> Generated: 2026-04-06 | Scan Level: Quick | Source: Pattern-based detection

## Overview

JPA/Hibernate entities persisted to H2 in-memory database (`jdbc:h2:mem:librarydb`).
ORM: Hibernate 5.0.12.Final via Spring Data JPA.
DDL strategy: `spring.jpa.hibernate.ddl-auto=update`

---

## Entities

### Book (`books` table)

- **Source:** `src/main/java/hu/example/library/model/Book.java`
- **Annotations:** `@Entity`, `@Table(name = "books")`
- **Key patterns:** Uses `javax.persistence.*`, `java.util.Date`, `SimpleDateFormat`

### Member (`members` table)

- **Source:** `src/main/java/hu/example/library/model/Member.java`
- **Annotations:** `@Entity`, `@Table(name = "members")`
- **Key patterns:** Uses `javax.validation.*`, `@Temporal`

### Loan (`loans` table)

- **Source:** `src/main/java/hu/example/library/model/Loan.java`
- **Annotations:** `@Entity`, `@Table(name = "loans")`
- **Key patterns:** Uses `Calendar`-based date calculation

---

## Relationships

| From | To | Type | Join Column |
|------|----|------|-------------|
| Loan | Book | `@ManyToOne` | `book_id` (NOT NULL) |
| Loan | Member | `@ManyToOne` | `member_id` (NOT NULL) |

---

## Repositories (Spring Data JPA)

| Repository | Entity | Source |
|------------|--------|--------|
| BookRepository | Book | `repository/BookRepository.java` |
| MemberRepository | Member | `repository/MemberRepository.java` |
| LoanRepository | Loan | `repository/LoanRepository.java` |

---

## Seed Data

- **Source:** `src/main/resources/data.sql`
- Pre-loads sample data on application startup

---

## Summary

- **Tables: 3** (books, members, loans)
- **Relationships: 2** (Loan → Book, Loan → Member)
- **Database: H2 in-memory** (development only)
- **ORM: Hibernate 5.0.x** via Spring Data JPA
- **Validation: javax.validation** (Bean Validation 1.1)
