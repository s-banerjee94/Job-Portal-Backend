# Job Portal Backend API

A REST API for a job portal where recruiters can post jobs and candidates can search and apply for positions.

## Technologies

- Java 17
- Spring Boot 3.5.7
- Spring Security with JWT
- Spring Data JPA
- MySQL
- Maven

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
- `GET /api/jobs` - List all jobs
- `GET /api/jobs/{id}` - Get job details
- `GET /api/jobs/search?skill=...&location=...` - Search jobs

### Candidates (Requires ROLE_CANDIDATE)
- `GET /api/candidates/profile` - Get profile
- `PUT /api/candidates/profile` - Update profile
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

## Configuration

Update `application.properties` with your MySQL credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```
