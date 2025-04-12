package com.sila.controller;

import com.sila.exception.BadRequestException;
import com.sila.service.CloudinaryService;
import com.sila.util.annotation.ValidFile;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
@Tag(name = "Cloudinary Controller", description = "Operations related to cloudinary")
@RequiredArgsConstructor

public class CloudinaryController {

    private final CloudinaryService cloudinaryService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") @ValidFile(
            allowedTypes = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE}) MultipartFile file) {
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
