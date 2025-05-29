package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.dto.response.UserResponse;
import com.sila.model.Restaurant;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RestaurantService {
    RestaurantResponse create(RestaurantRequest restaurantRequest);

    RestaurantResponse update(RestaurantRequest updateRestaurant, Long restaurantId)throws Exception;

    MessageResponse delete(Long id) throws Exception;

    Restaurant getById(Long id) throws Exception;

    RestaurantResponse getByUserLogin();

    List<FavoriteResponse> addFav(Long restaurantId);
    
    EntityResponseHandler<RestaurantResponse> search(Pageable pageable, SearchRequest searchReq);

    List<UserResponse> getUsersWhoOrderedFromRestaurant(Long restaurantId);

    Long all();
}
