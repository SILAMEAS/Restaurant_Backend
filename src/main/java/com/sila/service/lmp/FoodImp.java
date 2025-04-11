package com.sila.service.lmp;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.exception.BadRequestException;
import com.sila.exception.ImageUploadException;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.model.image.ImageFood;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.service.CategoryService;
import com.sila.service.CloudinaryService;
import com.sila.service.FoodService;
import com.sila.specifcation.FoodSpecification;
import com.sila.util.Utils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodImp implements FoodService {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    public Food create(FoodRequest foodRequest, Category category, Restaurant restaurant, List<MultipartFile> imageFiles) {
        Food food = Food.builder().name(foodRequest.getName()).description(foodRequest.getDescription()).price(foodRequest.getPrice()).available(foodRequest.isAvailable()).isVegetarian(foodRequest.isVegetarian()).isSeasonal(foodRequest.isSeasonal()).creationDate(new Date()).category(category).restaurant(restaurant).build();

        List<ImageFood> imageEntities = imageFiles.stream().map(file -> {
            try {
                String imageUrl = cloudinaryService.uploadFile(file);
                ImageFood image = new ImageFood();
                image.setUrl(imageUrl);
                image.setFood(food);
                return image;
            } catch (IOException e) {
                throw new ImageUploadException("Failed to upload image to Cloudinary", e);
            }
        }).toList();

        food.setImages(imageEntities);
        Food savedFood = foodRepository.save(food);
        restaurant.getFoods().add(savedFood);
        return savedFood;
    }

    @Override
    public Food update(FoodRequest foodReq, Long foodId) {
        Category category = categoryRepository.findById(foodReq.getCategoryId()).orElseThrow(() -> new BadRequestException("category not found"));
        Food food = getById(foodId);

        Utils.setIfNotNull(food.getName(), food::setName);
        Utils.setIfNotNull(food.getImages(), food::setImages);
        Utils.setIfNotNull(category, food::setCategory);
        Utils.setIfNotNull(food.getPrice(), food::setPrice);
        Utils.setIfNotNull(food.getDescription(), food::setDescription);
        Utils.setIfNotNull(food.isVegetarian(), food::setVegetarian);
        Utils.setIfNotNull(food.isSeasonal(), food::setSeasonal);
        Utils.setIfNotNull(food.isAvailable(), food::setAvailable);

        return foodRepository.save(food);
    }

    @Override
    public void delete(Long id) {
        Food foodByID = getById(id);
        foodByID.setRestaurant(null);
        foodRepository.deleteById(id);
    }

    @Override
    public String deleteByCategoryId(Long categoryId) {
        List<Food> foodsToDelete = foodRepository.findAllByCategoryId(categoryId);
        if (foodsToDelete.isEmpty()) {
            throw new BadRequestException("No food have category id : " + categoryId);
        }
        foodRepository.deleteAll(foodsToDelete);
        return "All food have categoryId : " + categoryId + " was deleted";
    }

    @Override
    public Food getById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new BadRequestException("NOT FOUND"));
    }

    @Override
    public Food updateStatus(Long id) {
        Food updateFood = getById(id);
        updateFood.setAvailable(!updateFood.isAvailable());
        return foodRepository.save(updateFood);
    }

    @Override
    public EntityResponseHandler<FoodResponse> gets(Pageable pageable, SearchRequest searchReq, String filterBy) {
        var foodPage = foodRepository.findAll(FoodSpecification.filterFood(searchReq, filterBy), pageable);
        return new EntityResponseHandler<>(foodPage.map(fs -> this.modelMapper.map(fs, FoodResponse.class)));
    }

    @Override
    public EntityResponseHandler<FoodResponse> getsByResId(Long restaurantId, Pageable pageable, SearchRequest searchReq, String filterBy) {
        var foodPage = foodRepository.findAll(FoodSpecification.filterFoodByRestaurantId(restaurantId, searchReq, filterBy), pageable);
        return new EntityResponseHandler<>(foodPage.map(fs -> this.modelMapper.map(fs, FoodResponse.class)));
    }


}
