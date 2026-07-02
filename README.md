# Loan Rate Matcher

[![CI](https://github.com/joanna-feng/loan-rate-matcher-v2/actions/workflows/ci.yml/badge.svg)](https://github.com/joanna-feng/loan-rate-matcher-v2/actions/workflows/ci.yml)

A full-stack web application that estimates a loan interest rate from a client's credit score and manages client records with full CRUD. Built with a **React** frontend and a **Spring Boot** REST API backed by **PostgreSQL**, deployed on **AWS** (EC2, RDS, S3) with a CI pipeline running **automated tests** on every push.

The rate-matching business logic is adapted from a simple Java Swing project built for UBC's CPSC 210 (Software Construction) course, preserved exactly while the storage and interface layers were rebuilt from scratch.

## Live Demo

- **Frontend:** http://loan-rate-matcher-frontend-293033346344.s3-website-us-west-2.amazonaws.com
- **Backend API:** http://54.185.104.120:8080/api/health (the root path `/` has no mapping and returns a 404 — hit `/api/health` or another endpoint from the [API Reference](#api-reference) below)

## Tech Stack

**Backend**
- Java 17, Spring Boot 4.1.0
- Maven (with Maven Wrapper — no local Maven install required)
- Spring Data JPA + PostgreSQL
- JUnit 5 + Mockito

**Frontend**
- React 19 + TypeScript
- Vite

**Infrastructure**
- AWS EC2 (backend, plain jar under systemd)
- AWS RDS for PostgreSQL (private, only reachable from the backend)
- AWS S3 (static frontend hosting)
- GitHub Actions (CI: backend tests + frontend build on every push)

## Architecture

```
frontend/          React + TypeScript app (Vite)
src/main/java/com/loanmatcher/
├── controller/     REST endpoints — HTTP in, JSON out, no business logic
├── service/        Business rules (loan rate tiers, client validation)
├── repository/     Spring Data JPA repositories
├── model/          JPA entities
└── config/         Cross-cutting config (CORS)
```

Dependencies flow one way: `controller → service → repository`. Business rules never leak into the controller layer, and repositories never call back up into services.

## Core Business Rule

Loan rate is determined by credit score, in three tiers (ported unchanged from the original Swing app):

| Credit score | Rate |
|---|---|
| < 600 | 10.0% |
| 600–699 | 7.0% |
| ≥ 700 | 4.5% |

## API Reference

| Method | Path | Description |
|---|---|---|
| GET | `/api/health` | Health check |
| GET | `/api/loan-rate?creditScore={n}` | Compute the loan rate for a credit score |
| POST | `/api/clients` | Create a client (`{name, creditScore}`); 409 if name is taken |
| GET | `/api/clients` | List all clients; optional `?minCreditScore={n}` filter |
| GET | `/api/clients/{id}` | Get a client by id; 404 if not found |
| PUT | `/api/clients/{id}` | Update a client's credit score (`{creditScore}`); 404 if not found |
| DELETE | `/api/clients/{id}` | Delete a client; 404 if not found |

## Running Locally

Requires Docker Desktop and Node.js. Java and Maven are handled by the included wrapper.

**1. Start the database**
```bash
docker compose up -d
```

**2. Run the backend** (from the project root)
```bash
./mvnw spring-boot:run
```
Backend runs on `http://localhost:8080`.

**3. Run the frontend**
```bash
cd frontend
npm install
npm run dev
```
Frontend runs on `http://localhost:5173` and talks to the local backend by default.

**4. Run backend tests**
```bash
./mvnw test
```

## Testing

13 automated tests (JUnit 5 + Mockito), run on every push via GitHub Actions:

- **`LoanRateService`** — the credit-score tiering logic is tested at the exact tier boundaries (599/600, 699/700), not just with values from the middle of each tier. Boundary values are where an off-by-one in `<` vs `<=` would actually break the rule, so they're the cases that matter most.
- **`ClientService`** — business rules with real branching (duplicate-name rejection on create, found-vs-not-found on update/delete) are unit tested with Mockito mocking `ClientRepository`, so they run in milliseconds without a database. Pure pass-through methods (e.g. `getAllClients`) are intentionally left untested — there's no logic there to verify.

## Deployment

- Backend: built with `./mvnw clean package`, the resulting jar is copied to an EC2 instance and run as a systemd service, connecting to RDS via environment variables (never committed to the repo).
- Frontend: built with `npm run build` (using `.env.production` for the API URL) and synced to an S3 static-website bucket with `aws s3 sync ./dist s3://<bucket> --delete`.
