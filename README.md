# JobTrackr

A full-stack Job Application Tracker built with Spring Boot and MySQL.

## What it does
- Register and login with secure JWT authentication
- Add, view, edit and delete job applications
- Track status: Applied, Interview, Offer, Rejected
- Dashboard with stats

## Tech Stack
- Backend: Java 21, Spring Boot 3.4.4
- Database: MySQL 8
- Security: Spring Security + JWT
- Frontend: HTML, CSS, JavaScript
- Build Tool: Maven

## API Endpoints

### Auth (Public)
- POST /api/auth/register - Create account
- POST /api/auth/login - Login and get JWT token

### Jobs (Protected - requires JWT token)
- POST /api/jobs - Add a job application
- GET /api/jobs/user/{userId} - Get all jobs for a user
- GET /api/jobs/{id} - Get one job
- PUT /api/jobs/{id} - Update a job
- DELETE /api/jobs/{id} - Delete a job

## How to Run

1. Clone the repository
   git clone https://github.com/skumari1340/Jobtrackr.git

2. Create the database
   CREATE DATABASE jobtrackr;

3. Copy and configure properties
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   Then add your MySQL password in that file.

4. Run the app
   ./mvnw clean spring-boot:run

5. Open browser at http://localhost:8080

## Project Structure
- model/      - Database entities
- repository/ - Database queries
- service/    - Business logic
- controller/ - API endpoints
- dto/        - Data transfer objects
- security/   - JWT authentication

## Author
Shweta Kumari
