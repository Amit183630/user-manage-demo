# <Project Name> ![Java](https://img.shields.io/badge/Java-17-red) ![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.2-brightgreen) [![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)

A robust user management system with registration, authentication, and admin features. Built with Spring Boot, JUnit, Swagger, and Docker.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Usage](#usage)
- [Testing](#testing)
- [Database Access](#database-access)
- [Project Structure](#project-structure)
- [Troubleshooting](#troubleshooting)
- [License](#license)

## Features
- ✅ User registration with email uniqueness check
- ✅ IP address detection and country lookup
- ✅ Basic Authentication security
- ✅ Admin-only endpoints for user management
- ✅ Swagger API documentation
- ✅ Docker containerization
- ✅ JUnit test coverage

## Prerequisites
- Java 17 JDK
- Maven 3.8+
- Docker (optional)
- IDE (STS/IntelliJ/VSCode)

## Installation

1. **Clone the repository**
```
   git clone https://github.com/<your-username>/<repository-name>.git
   cd <repository-name>
```

2. Build the project
    mvn clean install


## Configuration
Database Setup (src/main/resources/application.properties)

```
# H2 Database
spring.datasource.url=jdbc:h2:file:./data/userdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

Admin User Setup (src/main/resources/data.sql)
```
INSERT INTO users (name, email, gender, password, ip_address, country, roles)
SELECT 'Admin User', 'admin@example.com', 'Male', '$2a$04$4dyBN6OnPwEpGUD9KRFdRuVoJvJi0YWbRJIuE2MKpq96xhEYwvMfa', '127.0.0.1', 'Local', 'ROLE_ADMIN,ROLE_USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@example.com');
```

# Running the Application
Locally
```
mvn spring-boot:run
```

Docker
```
docker-compose up --build
```

# API Documentation
Access Swagger UI at:
http://localhost:8080/swagger-ui.html

Swagger UI

Usage
Authentication
Use Basic Authentication with:

Username: User's email

Password: User's password

Admin Credentials
Email: admin@example.com

Password: admin123

# Sample Requests
1. Register User
```
curl -X POST "http://localhost:8080/api/users/register" \
-H "Content-Type: application/json" \
-d '{
    "name": "John Doe",
    "email": "john@example.com",
    "gender": "Male",
    "password": "Password@123"
}'
```
2. Validate User
```
curl -u john@example.com:Password@123 \
-X POST "http://localhost:8080/api/users/validate"
```
3. Get All Users (Admin Only)
```
curl -u admin@example.com:admin123 \
-X GET "http://localhost:8080/api/users"
```

# Testing
Run Unit Tests
```
mvn test
```
Test Coverage Report
```
mvn jacoco:report
```
# Database Access
Access H2 Console at:
http://localhost:8080/h2-console
Use these credentials:
JDBC URL: jdbc:h2:file:./data/userdb
Username: test
Password: test

# Project Structure
```
<repository-name>/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/
│   │   │       ├── api/          # Integeration classes of https://www.ipify.org/ abd https://ip-api.com
│   │   │       ├── config/       # Configuration classes
│   │   │       ├── controller/   # REST Controllers
│   │   │       ├── dto/          # Data Transfer Objects
│   │   │       ├── model/        # Entity classes
│   │   │       ├── repository/   # JPA Repositories
│   │   │       ├── security/     # Security configurations
│   │   │       └── service/      # Business logic services
│   │   │       └── utils/        # Swagger messages utils
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql          # Initial data setup
├── test/                         # JUnit test classes
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```


# Happy Coding! 🚀
Reach out for support: amit183630@gmail.com
