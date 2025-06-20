# 🍽️ Restaurant Backend API

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
