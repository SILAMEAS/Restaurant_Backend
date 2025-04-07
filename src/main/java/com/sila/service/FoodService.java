package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import org.springframework.data.domain.Pageable;

public interface FoodService {
    Food createFood(FoodRequest food, Category category, Restaurant restaurant) throws Exception;

    Food updateFood(FoodRequest food, Long foodId) throws Exception;

    Void deleteFoodById(Long id) throws Exception;

    String deleteFoodByCategoryId(Long categoryId) throws Exception;

    Food findFoodById(Long foodId) throws Exception;

    Food updateAvailibilityStatus(Long id) throws Exception;

    EntityResponseHandler<FoodResponse> listFoods(Pageable pageable, SearchRequest searchReq, String filterBy);

    EntityResponseHandler<FoodResponse> listFoodsByRestaurantId(Long restaurantId, Pageable pageable, SearchRequest searchReq, String filterBy);

}
