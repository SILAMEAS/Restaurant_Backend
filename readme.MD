# 🍽️ Restaurant Backend API

https://wow-now-backend-springboot-jdke.onrender.com/swagger-ui/index.html

A scalable, production-ready RESTful API for a restaurant management system built with **Spring Boot**. This backend powers features such as menu management, user authentication, table reservations, orders, roles, and more.

---

## 🚀 Features

- ✅ JWT Authentication (Login/Register)
- 👥 Role-based access control (Admin, Staff, Customer)
- 🍔 Menu & Category Management
- 📅 Table & Reservation System
- 🛒 Order & Order Item Handling
- 📦 Product Stock & Status Updates
- 📈 Reporting Capabilities (optional future)
- 🌍 CORS Configuration for Frontend Integration
- 📄 Swagger UI Documentation

---

## 🛠️ Tech Stack

| Technology         | Purpose                          |
|--------------------|----------------------------------|
| **Spring Boot**    | Main application framework       |
| **Spring Security**| Authentication & authorization   |
| **JWT**            | Token-based auth                 |
| **Spring Data JPA**| ORM and database interaction     |
| **MySQL/PostgreSQL**| Database                        |
| **Lombok**         | Cleaner Java boilerplate         |
| **MapStruct**      | DTO mapping                      |
| **Swagger/OpenAPI**| API documentation                |
| **Maven**          | Build and dependency management  |

---

## 📁 Project Structure


    Restaurant_Backend/
    ├── src/
    │ ├── main/
    │ │ ├── java/com/example/restaurant/
    │ │ │ ├── config/ # Security and app config
    │ │ │ ├── controller/ # REST Controllers
    │ │ │ ├── dto/ # Data Transfer Objects
    │ │ │ ├── entity/ # JPA Entities
    │ │ │ ├── repository/ # Repositories
    │ │ │ ├── service/ # Business logic
    │ │ │ └── RestaurantApplication.java
    │ └── resources/
    │ ├── application.yml # App config
    │ └── data.sql # Sample seed data



---

## 📦 Getting Started

### 🔧 Prerequisites

- Java 17+
- Maven 3.8+
- MySQL or PostgreSQL
- IntelliJ / VS Code (recommended)

### 🧪 Setup and Run

```bash
# Clone the repository
git clone https://github.com/SILAMEAS/Restaurant_Backend.git
cd Restaurant_Backend

# Configure application.yml (DB credentials, JWT secret, etc.)
# Then run:
./mvnw spring-boot:run
