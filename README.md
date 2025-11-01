# Job Portal Backend API

A REST API for a job portal where recruiters can post jobs and candidates can search and apply for positions.

## Technologies

- Java 17
- Spring Boot 3.5.7
- Spring Security with JWT authentication
- Spring Data JPA with Hibernate
- Spring Cache (in-memory caching)
- MySQL 8.0
- Maven
- Swagger/OpenAPI 3.0 (Springdoc)
- Lombok

## Getting Started

### Prerequisites

- Java 17
- MySQL database running

### Run the Application

```bash
./mvnw spring-boot:run
```

### Build

```bash
./mvnw clean install
```

## Docker Setup

### Run with Docker Compose

The easiest way to run the application with MySQL:

```bash
docker-compose up -d
```

This will start both the MySQL database and the application. The app will be available at `http://localhost:8080`.

### Stop the containers

```bash
docker-compose down
```

### Build Docker image manually

```bash
docker build -t job-portal-api .
```

### Default Database Configuration (Docker)

- Database: `jobportal`
- Username: `jpbuser`
- Password: `jpbpass`
- Port: `3306`

## API Endpoints

### Authentication (Public)
- `POST /api/auth/register/candidate` - Register as candidate
- `POST /api/auth/register/recruiter` - Register as recruiter
- `POST /api/auth/login` - Login and get JWT token

### Jobs (Public)
- `GET /api/jobs` - List all jobs (supports pagination)
- `GET /api/jobs/{id}` - Get job details
- `GET /api/jobs/search?skill=...&location=...` - Search jobs

### Candidates (Requires ROLE_CANDIDATE)
- `GET /api/candidates/profile` - Get profile
- `PATCH /api/candidates/profile` - Update profile
- `POST /api/jobs/{id}/apply` - Apply to job

### Recruiters (Requires ROLE_RECRUITER)
- `POST /api/jobs` - Create job posting
- `GET /api/jobs/mine` - View own job postings
- `GET /api/applications/{jobId}` - View applicants for a job

## Authentication

Protected endpoints require a JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

## Features

- **Caching** - User authentication, candidate profiles, and job details are cached for better performance
- **Role-Based Access** - Separate permissions for candidates and recruiters
- **Experience Validation** - Automatic validation of minimum experience requirements when applying
- **Pagination** - Job listings support pagination and sorting
- **API Documentation** - Interactive Swagger UI available at `/swagger-ui/`

## Configuration

Update `application.yaml` or use environment variables:

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:jobportal}
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root}
```

For Docker deployments, set `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, and `DB_PASSWORD` as needed.
