# ✅ TODO List Application

A simple TODO list application built with Spring Boot (Kotlin) and Jetbrains Exposed ORM.

## 🔍 Overview

This project is a TODO list management application that allows users to create, update, delete, and track their tasks. It's built using modern technologies including Spring Boot framework with Kotlin and Jetbrains Exposed as the ORM solution.

## 🛠️ Technologies Used

- **Kotlin** - Primary programming language
- **Spring Boot** - Web application framework
- **Jetbrains Exposed** - SQL framework/ORM for Kotlin
- **Spring Data** - Data access layer abstractions

## ⭐ Features

- ✏️ Create, read, update, and delete tasks (CRUD operations)
- ✓ Mark tasks as completed
- 🔍 Filter tasks by status (completed, pending)
- 🔝 Task prioritization
- 🏷️ Task categorization

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/BortnikD/TODO_LIST.git
   ```

2. Navigate to the project directory:
   ```bash
   cd TODO_LIST
   ```

3. Build the project:
   ```bash
   ./gradlew build
   ```

4. Run the application:
   ```bash
   ./gradlew bootRun
   ```

The application will start on `localhost:8080` by default.

## 📊 Jetbrains Exposed ORM

This project uses Jetbrains Exposed, a lightweight SQL library for Kotlin:

- 🔒 Type-safe SQL DSL
- 📝 DAO (Data Access Objects) API
- 🔄 Easy database migrations
- 🔌 Connection pooling