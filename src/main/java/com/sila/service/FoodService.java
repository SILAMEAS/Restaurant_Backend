package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.FoodByRestaurantIdReq;
import com.sila.dto.request.PaginationReq;
import com.sila.dto.request.SearchReq;
import com.sila.dto.response.FoodRes;
import com.sila.dto.response.RestaurantRes;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.dto.request.FoodReq;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FoodService {
    public Food createFood(FoodReq food, Category category, Restaurant restaurant)throws Exception;
    public Void deleteFoodById(Long id)throws Exception;
    public Food findFoodById(Long foodId)throws Exception;
    public Food updateAvailibilityStatus(Long id)throws Exception;
    public EntityResponseHandler<FoodRes> listFoods(Pageable pageable, SearchReq searchReq, String filterBy);
    public EntityResponseHandler<FoodRes> listFoodsByRestaurantId(Long restaurantId,Pageable pageable, SearchReq searchReq,String filterBy);

}
