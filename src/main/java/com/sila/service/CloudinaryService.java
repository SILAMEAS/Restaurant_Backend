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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Service
public interface CloudinaryService {

    List<String> deleteImages(List<String> publicIds);


    Map<String, String> uploadFile(MultipartFile file); // Updated to return Map

    Map<String, String> uploadFileRemoveBG(MultipartFile file);

    String deleteImage(String publicId) throws IOException;
    String getBackgroundRemovedImage(String publicId);

    <T, I> List<I> uploadImagesToCloudinary(
            List<MultipartFile> imageFiles,
            T parent,
            BiFunction<String, String, I> imageFactory, // (url, publicId) -> new Image
            BiConsumer<I, T> setParent // (image, parent) -> image.setParent(parent)
    );

}
