package com.sila.service;

import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.model.image.ImageFood;
import com.sila.model.image.ImageRestaurant;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public interface CloudinaryService {

    List<String> deleteImages(List<String> publicIds);


    Map<String, String> uploadFile(MultipartFile file); // Updated to return Map

    String deleteImage(String publicId) throws IOException;

    List<ImageFood> uploadFoodImageToCloudinary(List<MultipartFile> imageFiles, Food food);

    List<ImageRestaurant> uploadRestaurantImageToCloudinary(List<MultipartFile> imageFiles, Restaurant restaurant);

}
