# library-management-system
Production-ready Library Management System using Spring Boot, Spring Security (JWT), MySQL, REST APIs, JUnit 5, Mockito, and JaCoCo Test Coverage.
# Library Management System

A secure REST API for managing library operations — book inventory, member registration, book issue/return with automated fine calculation, and role-based access control.

Built as a portfolio project to demonstrate backend development skills using Java, Spring Boot, and Spring Security.

## Features

- **JWT-based Authentication** — stateless login with role-based authorization (`ADMIN` / `MEMBER`)
- **Book Management** — full CRUD operations with search by title, author, and genre
- **Book Copy Tracking** — supports multiple physical copies per book with availability status
- **Issue & Return System** — tracks issue date, due date, and return date per transaction
- **Automated Fine Calculation** — ₹5/day fine computed automatically on late returns
- **Global Exception Handling** — consistent, clean JSON error responses (404, 400, 403) instead of raw stack traces
- **Role-Based Access Control** — `ADMIN` manages books and issues; `MEMBER` can only view and track their own records

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1 |
| Security | Spring Security + JWT (jjwt) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL |
| Testing | JUnit 5, Mockito, MockMvc |
| Build Tool | Maven |

## Architecture

```
Controller  ->  Service  ->  Repository  ->  Database
     |
Security Filter Chain (JWT Auth Filter)
     |
Global Exception Handler
```

Entities and relationships:

```
User (1) ---- (1) Member (1) ---- (M) IssueRecord (M) ---- (1) BookCopy (M) ---- (1) Book
```

## API Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| POST | /api/auth/register | Public | Register a new user |
| POST | /api/auth/login | Public | Login and receive JWT token |
| GET | /api/books | Authenticated | List all books |
| POST | /api/books | ADMIN | Add a new book |
| PUT | /api/books/{id} | ADMIN | Update a book |
| DELETE | /api/books/{id} | ADMIN | Delete a book |
| POST | /api/issues/issue | ADMIN | Issue a book to a member |
| PUT | /api/issues/return/{id} | ADMIN | Return a book (fine auto-calculated) |
| GET | /api/issues/overdue | ADMIN | List all overdue issues |

## Running Locally

1. Clone the repository
2. Update application.properties with your MySQL credentials
3. Run:
   ```
   mvn spring-boot:run
   ```
4. API available at http://localhost:8080

## Running Tests

```
mvn clean test
```

Coverage report generated at target/site/jacoco/index.html

## Author

Ankit Roy
[GitHub](https://github.com/J0rd0n9386) - [LinkedIn](https://linkedin.com/in/ankit-roy-b57848196)
