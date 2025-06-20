# Restaurant Backend API

A robust backend RESTful API for managing a restaurant system built with **Spring Boot**.

---

## Table of Contents

- [About](#about)  
- [Features](#features)  
- [Tech Stack](#tech-stack)  
- [Getting Started](#getting-started)  
  - [Prerequisites](#prerequisites)  
  - [Installation](#installation)  
  - [Running the Application](#running-the-application)  
- [API Documentation](#api-documentation)  
- [Project Structure](#project-structure)  
- [Contributing](#contributing)  
- [License](#license)  
- [Contact](#contact)  

---

## About

This backend API is designed to manage various aspects of a restaurant, including menu management, order processing, table reservations, and user authentication. The system provides secure, scalable REST endpoints, making it suitable for integration with web or mobile frontends.

---

## Features

- User registration, login, and role-based access control (e.g., admin, staff, customer)  
- CRUD operations for menu items, categories, and ingredients  
- Order management with status tracking  
- Table management and reservation system  
- Secure password hashing using BCrypt  
- JWT-based authentication and authorization  
- Exception handling and input validation  
- Integration with PostgreSQL (or other relational databases)  
- API documentation with Swagger/OpenAPI  

---

## Tech Stack

| Technology           | Description                           |
|---------------------|-------------------------------------|
| Java 17+            | Programming language                 |
| Spring Boot         | Application framework                |
| Spring Security     | Authentication & Authorization      |
| Spring Data JPA     | Database access layer                |
| PostgreSQL          | Relational database                  |
| Maven               | Build and dependency management     |
| JWT                 | JSON Web Tokens for security        |
| Swagger/OpenAPI     | API documentation                   |

---

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher  
- Maven 3.6+  
- PostgreSQL installed and running  
- IDE such as IntelliJ IDEA or Eclipse (optional)  

### Installation

1. Clone this repository:

   ```bash
   git clone https://github.com/SILAMEAS/Restaurant_Backend.git
   cd Restaurant_Backend

    spring.datasource.url=jdbc:postgresql://localhost:5432/restaurant_db
    spring.datasource.username=your_db_username
    spring.datasource.password=your_db_password
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    
    jwt.secret=your_jwt_secret_key
    jwt.expirationMs=3600000

    mvn clean install

    mvn spring-boot:run

    http://localhost:8080/swagger-ui/index.html

### Project Structure


    src/
     ├─ main/
     │   ├─ java/com/yourorg/restaurant/
     │   │    ├─ controller/       # REST Controllers (API endpoints)
     │   │    ├─ service/          # Business logic layer
     │   │    ├─ repository/       # Database repositories (JPA)
     │   │    ├─ model/            # JPA entities and domain models
     │   │    ├─ security/         # JWT & Spring Security configuration
     │   │    ├─ exception/        # Custom exceptions and handlers
     │   │    └─ config/           # App configuration classes
     │   └─ resources/
     │        ├─ application.properties  # Spring Boot config
     │        └─ db/migration               # Flyway or Liquibase migration scripts (if used)









### Integrate sprint boot and cloudinary

✅ 1. Create a Cloudinary Account
Sign up at https://cloudinary.com/

From the Dashboard, get your Cloud Name, API Key, and API Secret

✅ 2. Add Cloudinary SDK to Spring Boot Project
If you're using Maven, add this to your pom.xml:

    <dependency>
      <groupId>com.cloudinary</groupId>
      <artifactId>cloudinary-http44</artifactId>
      <version>1.33.0</version> <!-- or latest -->
    </dependency>

✅ 3. Configure Cloudinary in Spring Boot

Create a configuration class:

    import com.cloudinary.Cloudinary;
    import com.cloudinary.utils.ObjectUtils;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    
    import java.util.Map;
    
    @Configuration
    public class CloudinaryConfig {
    
        @Bean
        public Cloudinary cloudinary() {
            return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "YOUR_CLOUD_NAME",
                "api_key", "YOUR_API_KEY",
                "api_secret", "YOUR_API_SECRET",
                "secure", true
            ));
        }
    }
✅ 4. Service to Upload Files

    import com.cloudinary.Cloudinary;
    import com.cloudinary.utils.ObjectUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    
    import java.io.IOException;
    import java.util.Map;
    
    @Service
    public class CloudinaryService {
    
        @Autowired
        private Cloudinary cloudinary;
    
        public String uploadFile(MultipartFile file) throws IOException {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return (String) uploadResult.get("secure_url");
        }
    }
✅ 5. Controller to Handle Upload

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
    
    @RestController
    @RequestMapping("/api/media")
    public class MediaController {
    
        @Autowired
        private CloudinaryService cloudinaryService;
    
        @PostMapping("/upload")
        public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
            try {
                String url = cloudinaryService.uploadFile(file);
                return ResponseEntity.ok(url);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
            }
        }
    }
