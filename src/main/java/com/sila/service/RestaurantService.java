package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.RestaurantReq;
import com.sila.dto.request.SearchReq;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.RestaurantRes;
import com.sila.model.Restaurant;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface RestaurantService {
    Restaurant createRestaurant(RestaurantReq req) throws Exception;
    Restaurant updateRestaurant(RestaurantReq updateRestaurant, Long restaurantId) throws Exception;
    void deleteRestaurant(Long id)throws Exception;
    Restaurant findRestaurantById(Long id) throws Exception;
    Restaurant getRestaurantByUserId(Long userId) throws Exception;
    List<FavoriteResponse> addRestaurantToFavorites(Long restaurantId, User user)throws Exception;
    Restaurant updateRestaurantStatus(Long restaurantId)throws Exception;
    EntityResponseHandler<RestaurantRes>  searchRestaurant(Pageable pageable, SearchReq searchReq);
}
