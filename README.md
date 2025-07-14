# 📚 Book Rental System

A Java Spring Boot application for managing book rentals. Users can view available books, rent and return them, and browse rental history.

---

## 🚀 Technologies Used

* Java 21
* Spring Boot
* Spring Data JPA
* PostgreSQL
* Maven
* Docker & Docker Compose
* Kubernetes (Minikube)
* Swagger / OpenAPI
* Clean Architecture principles

---

## ⚙️ Getting Started

### 🔧 Prerequisites

* Java 21
* Maven 3.8+
* Docker
* (Optional) Minikube for Kubernetes

---

### ▶️ Running locally with Maven

```bash
# Build the project
./mvnw clean install

# Run the app
./mvnw spring-boot:run
```

App will be available at: [http://localhost:8080](http://localhost:8080)

Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

---

### 🐳 Running with Docker

```bash
docker-compose up --build
```

* App: [http://localhost:8080](http://localhost:8080)
* PostgreSQL: exposed on port `5432`

---

### ☸️ Running in Kubernetes (Minikube)

1. Start Minikube:

   ```bash
   minikube start
   ```

2. Build Docker image inside Minikube:

   ```bash
   minikube image build -t book-rental-app .
   ```

3. Apply manifests:

   ```bash
   kubectl apply -f k8s/
   ```

4. Access app:

   ```bash
   minikube service book-rental
   ```

---

## 📑 API Endpoints

Some key endpoints:

| Method | Endpoint                                 | Description             |
| ------ | ---------------------------------------- | ----------------------- |
| GET    | `/api/books`                             | List all books          |
| POST   | `/api/books`                             | Add new book            |
| GET    | `/api/users`                             | List all users          |
| POST   | `/api/users`                             | Add new user            |
| GET    | `/api/users/{id}`                        | List user by ID         |
| GET    | `/api/users/name/{name}`                 | List user by name       |
| GET    | `/api/rentals/rent`                      | List all rents          |
| POST   | `/api/rentals/rent`                      | Rent a book             |
| POST   | `/api/rentals/return/{id}`               | Return a rented book    |
| GET    | `/api/rentals/user/{userId}?filter=`     | Get user rental history (ALL / ACTIVE / RETURNED) |
| GET    | `/api/rentals/book/{bookId}?filter=`     | Get user rental history (ALL / ACTIVE / RETURNED) |

---

## ✅ Features

* User, Book and Rental management
* Active/returned rental filtering
* Global error handling
* DTOs + mappers for clean API responses
* Validation with custom messages
* Email notifications
* Swagger/OpenAPI integration
* Docker & K8s ready

---

## 📁 Project Structure

```
src/
├── config/            # Config (Error handling)
├── controller/        # REST Controllers
├── data/
      ├── entity/      # REST Controllers
      ├── mapper/      # REST Controllers
      ├── request/     # REST Controllers
      ├── response/    # REST Controllers
├── notification/      # Email Notification
├── repository/        # Spring Data JPA repositories
├── service/           # Business logic
└── BookRentalSystemApplication.java
```

---

## 📜 License

This project is private and intended for personal or educational use.

---
