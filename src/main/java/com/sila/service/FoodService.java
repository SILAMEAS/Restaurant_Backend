package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface FoodService {
    Food create(FoodRequest food, Category category, Restaurant restaurant, List<MultipartFile> imageFiles);

    Food update(FoodRequest food, Long foodId);

    void delete(Long id) ;

    String deleteByCategoryId(Long categoryId) ;

    Food getById(Long foodId);

    Food updateStatus(Long id);

    EntityResponseHandler<FoodResponse> gets(Pageable pageable, SearchRequest searchReq, String filterBy);

    EntityResponseHandler<FoodResponse> getsByResId(Long restaurantId, Pageable pageable, SearchRequest searchReq, String filterBy);

}
