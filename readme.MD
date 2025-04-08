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
