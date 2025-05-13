# ✅ TODO List Application

A modern TODO list application built with Spring Boot (Kotlin) and Jetbrains Exposed ORM.

## 🔍 Overview

This project is a TODO list management application that allows users to create, update, delete, and track their tasks. It's built using modern technologies including Spring Boot framework with Kotlin and Jetbrains Exposed ORM for database interactions.

## 🛠️ Technologies Used

- **Kotlin** - Primary programming language
- **Spring Boot** - Web application framework
- **Jetbrains Exposed** - SQL framework/ORM for Kotlin
- **Spring Data** - Data access layer abstractions
- **JWT** - Token-based authentication
- **Dragonfly** - High-performance in-memory cache
- **PostgreSQL** - Database storage
- **SpringDoc OpenAPI** - API documentation

## 🔐 Security

The application uses JWT (JSON Web Token) authentication to secure endpoints:
- All requests (except auth endpoints) require a valid JWT token
- Tokens must be included in the Authorization header
- BCrypt password encoding for secure password storage
- Session management with stateless configuration

## ⚡ Cache Implementation

The application uses Dragonfly as a high-performance caching solution:
- Faster and more memory-efficient alternative to Redis
- Cache entries automatically expire after 10 minutes
- Cached data includes:
   - User information
   - Task lists (completed and uncompleted)
   - Category information
   - Search results

## ⭐ Features

- ✏️ Create, read, update, and delete tasks (CRUD operations)
- ✓ Mark tasks as completed
- 🔍 Filter tasks by status (completed, pending)
- 🔝 Task prioritization
- 🏷️ Task categorization
- 🔒 User authentication and authorization
- 📝 API documentation via Swagger UI

## 📚 API Documentation

The API documentation is available at: [localhost/swagger-ui/index.html](http://localhost/swagger-ui/index.html)

This interactive documentation allows you to:
- View all available endpoints
- Test API calls directly from the browser
- See request/response schemas and examples
- Understand authentication requirements

## 🚀 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/BortnikD/TODO-list.git
   ```
2. Set environment variables in .env file
   ```code 
   JWT_SECRET_KEY=YOUR_JWT_SECRET_KEY
   DB_PASSWORD=YOUR_DB_PASSWORD
   ```
3. Navigate to the project directory:
   ```bash
   cd TODO-list
   ```
4. Build the project:
   ```bash
   ./gradlew build
   ```
5. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## 🐳 Docker Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/BortnikD/TODO-list.git
   ```

2. Set environment variables in .env file
   ```code 
   JWT_SECRET_KEY=YOUR_JWT_SECRET_KEY
   DB_PASSWORD=YOUR_DB_PASSWORD
   ```

3. Navigate to the project directory:
   ```bash
   cd TODO-list
   ```
   
4. Build and run with Docker Compose:
   ```bash
   docker-compose up --build
   ```
   
This will start the application with all necessary dependencies (PostgreSQL and Dragonfly) in Docker containers.

The application will start on [localhost:8080](http://localhost:8080) by default.

## 🔑 Authentication

To use the API, you'll need to:

1. Register a new user:
   ```code
   POST /api/auth/register
   ```

2. Login to get a JWT token:
   ```code
   POST /api/auth/login
   ```
   
3. Include the token in subsequent requests:
   ```code
   Authorization: Bearer <your_token_here>
   ```
   
## 📊 Jetbrains Exposed ORM  
This project uses Jetbrains Exposed, a lightweight SQL library for Kotlin:

- 🔒 Type-safe SQL DSL  
- 📝 DAO (Data Access Objects) API  
- 🔄 Easy database migrations  
- 🔌 Connection pooling  
