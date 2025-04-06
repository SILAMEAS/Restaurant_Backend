package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.dto.request.FoodRequest;

import org.springframework.data.domain.Pageable;

public interface FoodService {
    public Food createFood(FoodRequest food, Category category, Restaurant restaurant)throws Exception;
    public Food updateFood(FoodRequest food, Long foodId)throws Exception;
    public Void deleteFoodById(Long id)throws Exception;
    public String deleteFoodByCategoryId(Long categoryId)throws Exception;
    public Food findFoodById(Long foodId)throws Exception;
    public Food updateAvailibilityStatus(Long id)throws Exception;
    public EntityResponseHandler<FoodResponse> listFoods(Pageable pageable, SearchRequest searchReq, String filterBy);
    public EntityResponseHandler<FoodResponse> listFoodsByRestaurantId(Long restaurantId, Pageable pageable, SearchRequest searchReq, String filterBy);

}
