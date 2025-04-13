package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.model.Restaurant;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface RestaurantService {
    Restaurant create(RestaurantRequest restaurantRequest, List<MultipartFile> imageRestaurants);

    Restaurant update(RestaurantRequest updateRestaurant, Long restaurantId,List<MultipartFile> imageRestaurants)throws Exception;

    void delete(Long id) throws Exception;

    Restaurant getById(Long id) throws Exception;

    Restaurant getByUserLogin();

    List<FavoriteResponse> addFav(Long restaurantId, User user) throws Exception;
    
    EntityResponseHandler<RestaurantResponse> search(Pageable pageable, SearchRequest searchReq);
}
