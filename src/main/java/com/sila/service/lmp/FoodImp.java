package com.sila.service.lmp;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.service.FoodService;
import com.sila.specifcation.FoodSpecification;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FoodImp implements FoodService {

    private final FoodRepository foodRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public Food createFood(FoodRequest foodRequest, Category category, Restaurant restaurant) {
        Food food = Food.builder().category(category).name(foodRequest.getName()).
                restaurant(restaurant).description(foodRequest.getDescription()).images(foodRequest.getImages()).
                name(foodRequest.getName()).price(foodRequest.getPrice()).creationDate(new Date()).isSeasonal(foodRequest.isSeasonal()).isVegetarian(foodRequest.isVegetarian())
                .available(foodRequest.isAvailable())
                .build();
        Food saveFood = foodRepository.save(food);
        restaurant.getFoods().add(saveFood);
        return saveFood;
    }

    @Override
    public Food updateFood(FoodRequest foodReq, Long foodId) {
        Category category = categoryRepository.findById(foodReq.getCategoryId()).orElseThrow(() -> new BadRequestException("category not found"));
        Food food = findFoodById(foodId);
    if(!Objects.isNull(food.getName())){
        food.setName(food.getName());
    }
    if(!Objects.isNull(food.getImages())){
        food.setImages(food.getImages());
    }
    if(!Objects.isNull(food.getCategory())){
        food.setCategory(category);
    }
    if(!Objects.isNull(food.getPrice())){
        food.setPrice(food.getPrice());
    }
    if(!Objects.isNull(food.getDescription())){
        food.setDescription(food.getDescription());
    }
        food.setVegetarian(food.isVegetarian());
        food.setSeasonal(food.isSeasonal());
        food.setAvailable(food.isAvailable());

        return foodRepository.save(food);
    }

    @Override
    public void deleteFoodById(Long id)  {
        Food foodByID = findFoodById(id);
        foodByID.setRestaurant(null);
        foodRepository.deleteById(id);
    }

    @Override
    public String deleteFoodByCategoryId(Long categoryId) {
        List<Food> foodsToDelete = foodRepository.findAllByCategoryId(categoryId);
        if (foodsToDelete.isEmpty()) {
            throw new BadRequestException("No food have category id : " + categoryId);
        }
        foodRepository.deleteAll(foodsToDelete);
        return "All food have categoryId : " + categoryId + " was deleted";
    }

    @Override
    public Food findFoodById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() -> new BadRequestException("NOT FOUND"));
    }

    @Override
    public Food updateAvailibilityStatus(Long id) {
        Food updateFood = findFoodById(id);
        updateFood.setAvailable(!updateFood.isAvailable());
        return foodRepository.save(updateFood);
    }

    @Override
    public EntityResponseHandler<FoodResponse> listFoods(Pageable pageable, SearchRequest searchReq, String filterBy) {
        var foodPage = foodRepository.findAll(FoodSpecification.filterFood(searchReq, filterBy), pageable);
        return new EntityResponseHandler<>(foodPage
                .map(fs -> this.modelMapper.map(fs, FoodResponse.class)));
    }

    @Override
    public EntityResponseHandler<FoodResponse> listFoodsByRestaurantId(Long restaurantId, Pageable pageable, SearchRequest searchReq, String filterBy) {
        var foodPage = foodRepository.findAll(FoodSpecification.filterFoodByRestaurantId(restaurantId, searchReq, filterBy), pageable);
        return new EntityResponseHandler<>(foodPage
                .map(fs -> this.modelMapper.map(fs, FoodResponse.class)));
    }


}
