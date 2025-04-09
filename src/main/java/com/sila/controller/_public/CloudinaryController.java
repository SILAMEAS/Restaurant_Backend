package com.sila.controller._public;

import com.sila.service.CloudinaryService;
import com.sila.exception.BadRequestException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@Tag(name = "Cloudinary Controller", description = "Operations related to cloudinary")
@RequiredArgsConstructor

public class CloudinaryController {

    final private CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @DeleteMapping("/{publicId}")
    public ResponseEntity<String> deleteImage(@PathVariable String publicId) {
        try {
            String result = cloudinaryService.deleteImage(publicId);
            return ResponseEntity.ok("✅ Image deleted: " + result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ Error deleting image: " + e.getMessage());
        }
    }
}
