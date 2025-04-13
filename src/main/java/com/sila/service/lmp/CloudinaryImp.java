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
import java.util.List;

@Service
@RequiredArgsConstructor
public class CloudinaryImp implements CloudinaryService {

    private static final Logger logger = LoggerFactory.getLogger(CloudinaryImp.class);
    private final Cloudinary cloudinary;

    @Override
    public String uploadFile(MultipartFile file) {
        try {
            var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String secureUrl = (String) uploadResult.get("secure_url");
            logger.info("File uploaded successfully: {}", secureUrl);
            return secureUrl;
        } catch (IOException e) {
            // Log and rethrow the exception with contextual information
            String errorMessage = String.format("Failed to upload file: %s", file.getOriginalFilename());
            logger.error(errorMessage, e);
            throw new BadRequestException(errorMessage + e);
        } catch (Exception e) {
            // Log and rethrow unexpected exceptions
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
            // Log and rethrow the exception with contextual information
            String errorMessage = String.format("Failed to delete image with public ID: %s", publicId);
            logger.error(errorMessage, e);
            throw new BadRequestException(errorMessage + e);
        } catch (Exception e) {
            // Log and rethrow unexpected exceptions
            String errorMessage = String.format("Unexpected error occurred while deleting image with public ID: %s", publicId);
            logger.error(errorMessage, e);
            throw new BadRequestException(errorMessage + e);
        }
    }

    /**
     * Method : use to upload image to cloudinary before saving to food
     **/
    @Override
    public List<ImageFood> uploadFoodImageToCloudinary(List<MultipartFile> imageFiles, Food food) {
        return imageFiles.stream().map(file -> {
            String imageUrl = uploadFile(file);
            ImageFood image = new ImageFood();
            image.setUrl(imageUrl);
            image.setFood(food);
            return image;
        }).toList();
    }

    /**
     * Method : use to upload image to cloudinary before saving to food
     **/
    @Override
    public List<ImageRestaurant> uploadRestaurantImageToCloudinary(List<MultipartFile> imageFiles, Restaurant restaurant) {
        return imageFiles.stream().map(file -> {
            String imageUrl = uploadFile(file);
            return ImageRestaurant.builder().url(imageUrl).restaurant(restaurant).build();
        }).toList();
    }


}
