
package com.sila.service.lmp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sila.exception.BadRequestException;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.model.image.ImageFood;
import com.sila.model.image.ImageRestaurant;
import com.sila.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloudinaryImp implements CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryImp.class);
    private final Cloudinary cloudinary;

    @Override
    public Map<String, String> uploadFile(MultipartFile file) {
        try {
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String publicId = (String) uploadResult.get("public_id");
            String secureUrl = (String) uploadResult.get("secure_url");
            logger.info("File uploaded successfully with publicId: {} and secureUrl: {}", publicId, secureUrl);
            Map<String, String> result = new HashMap<>();
            result.put("publicId", publicId);
            result.put("secureUrl", secureUrl);
            return result;
        } catch (IOException e) {
            String errorMessage = String.format("Failed to upload file: %s", file.getOriginalFilename());
            logger.error(errorMessage, e);
            throw new BadRequestException(errorMessage);
        } catch (Exception e) {
            String errorMessage = String.format("Unexpected error occurred while uploading file: %s", file.getOriginalFilename());
            logger.error(errorMessage, e);
            throw new RuntimeException(errorMessage, e);
        }
    }

    @Override
    public String deleteImage(String publicId) {
        try {
            var result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            logger.info("Image deleted successfully: {}", publicId);
            return result.toString();
        } catch (IOException e) {
            String errorMessage = String.format("Failed to delete image with public ID: %s", publicId);
            logger.error(errorMessage, e);
            throw new BadRequestException(errorMessage + e);
        } catch (Exception e) {
            String errorMessage = String.format("Unexpected error occurred while deleting image with public ID: %s", publicId);
            logger.error(errorMessage, e);
            throw new BadRequestException(errorMessage + e);
        }
    }

    @Override
    public List<String> deleteImages(List<String> publicIds) {
        return publicIds.stream().map(publicId -> {
            try {
                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                logger.info("Image deleted successfully: {}", publicId);
                return "✅ Deleted: " + publicId + " - Result: " + result.toString();
            } catch (IOException e) {
                String errorMessage = String.format("Failed to delete image with public ID: %s", publicId);
                logger.error(errorMessage, e);
                return "❌ Failed: " + publicId + " - " + errorMessage;
            } catch (Exception e) {
                String errorMessage = String.format("Unexpected error occurred while deleting image with public ID: %s", publicId);
                logger.error(errorMessage, e);
                return "❌ Failed: " + publicId + " - " + errorMessage;
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<ImageFood> uploadFoodImageToCloudinary(List<MultipartFile> imageFiles, Food food) {
        return imageFiles.stream().map(file -> {
            Map<String, String> uploadResult = uploadFile(file);
            String imageUrl = uploadResult.get("secureUrl");
            String publicId = uploadResult.get("publicId");
            ImageFood image = new ImageFood();
            image.setUrl(imageUrl);
            image.setPublicId(publicId);
            image.setFood(food);
            return image;
        }).toList();
    }

    @Override
    public List<ImageRestaurant> uploadRestaurantImageToCloudinary(List<MultipartFile> imageFiles, Restaurant restaurant) {
        return imageFiles.stream().map(file -> {
            Map<String, String> uploadResult = uploadFile(file);
            String imageUrl = uploadResult.get("secureUrl");
            String publicId = uploadResult.get("publicId");
            var image = new ImageRestaurant();
            image.setUrl(imageUrl);
            image.setRestaurant(restaurant);
            image.setPublicId(publicId);
            return image;
        }).toList();
    }
}