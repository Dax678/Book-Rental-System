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

| Method | Endpoint                                 | Description                              |
| ------ | ---------------------------------------- | ---------------------------------------- |
| POST   | `/api/auth/signup`                       | Create new account                       |
| POST   | `/api/auth/login`                        | Login into account                       |
| GET    | `/api/transactions`                      | List all transactions 🔒                 |
| GET    | `/api/transactions/book/{bookId}`        | Get transactions by book ID 🔒           |
| GET    | `/api/transactions/me`                   | Get transactions for logged-in user 🔒   |
| POST   | `/api/transactions/rent`                 | Rent a book 🔒                           |
| POST   | `/api/transactions/return/{rentalId}`    | Return a rented book 🔒                  |
| GET    | `/api/transactions/user/{userId}`        | Get transactions by user ID 🔒           |
| GET    | `/api/books`                             | List all books                           |
| POST   | `/api/books/save`                        | Add or update a book 🔒                  |
| GET    | `/api/books/{id}`                        | Get book by ID                           |
| GET    | `/api/promotions`                        | List all promotions                      |
| POST   | `/api/promotions/save`                   | Add or update a promotion 🔒             |
| GET    | `/api/prices/history`                    | Get book price history                   |
| GET    | `/api/users`                             | List all users 🔒                        |
| GET    | `/api/users/me`                          | Get details of logged-in user 🔒         |
| GET    | `/api/users/name/{name}`                 | Get users by name 🔒                     |
| GET    | `/api/users/{id}`                        | Get user by ID 🔒                        |
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
