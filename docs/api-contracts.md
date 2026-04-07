# API Contracts — Library API

> Generated: 2026-04-06 | Scan Level: Quick | Source: Pattern-based detection

## Overview

REST API served via Spring MVC under context path `/library`. All endpoints prefixed with `/api/`.

Base URL: `http://localhost:8081/library`

Swagger UI: `http://localhost:8081/library/swagger-ui.html`

---

## Book Controller

Base path: `/api/books`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/books` | List all books |
| GET | `/api/books/{id}` | Get book by ID |
| POST | `/api/books` | Create a new book |
| PUT | `/api/books/{id}` | Update a book |
| DELETE | `/api/books/{id}` | Delete a book |
| GET | `/api/books/available` | List available (not borrowed) books |
| GET | `/api/books/search?keyword=` | Search books by keyword |

**Total endpoints: 7**

---

## Loan Controller

Base path: `/api/loans`

| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/loans/borrow?bookId=&memberId=` | Borrow a book |
| POST | `/api/loans/{id}/return` | Return a borrowed book |
| GET | `/api/loans/active` | List active (unreturned) loans |
| GET | `/api/loans/overdue` | List overdue loans |
| GET | `/api/loans/member/{memberId}` | List loans for a specific member |

**Total endpoints: 5**

---

## Summary

- **Total API endpoints: 12**
- **Controllers: 2** (BookController, LoanController)
- **Authentication: None** (no auth middleware detected)
- **API Documentation: Springfox Swagger 2.9.2**
- **Validation: javax.validation (Hibernate Validator)**
- **Error handling: Manual HashMap-based error responses in LoanController**
