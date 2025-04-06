package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.model.Restaurant;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RestaurantService {
    Restaurant createRestaurant(RestaurantRequest req) throws Exception;
    Restaurant updateRestaurant(RestaurantRequest updateRestaurant, Long restaurantId) throws Exception;
    void deleteRestaurant(Long id)throws Exception;
    Restaurant findRestaurantById(Long id) throws Exception;
    Restaurant getRestaurantByUserId(Long userId) throws Exception;
    List<FavoriteResponse> addRestaurantToFavorites(Long restaurantId, User user)throws Exception;
    Restaurant updateRestaurantStatus(Long restaurantId)throws Exception;
    EntityResponseHandler<RestaurantResponse>  searchRestaurant(Pageable pageable, SearchRequest searchReq);
}
