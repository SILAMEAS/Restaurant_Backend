package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.UserRequest;
import com.sila.dto.response.AddressResponse;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.UserResponse;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    User getByJwt(String jwt) throws Exception;

    User getByEmail(String email);

    User getById(Long userId);

    EntityResponseHandler<UserResponse> list(Pageable pageable, String search) throws Exception;

    UserResponse update( UserRequest userReq) throws Exception;

    UserResponse getProfile() throws Exception;
    Long all();
    EntityResponseHandler<UserResponse> getUsersWhoOrderedFromRestaurant(Long restaurantId, Pageable pageable);
    Long allHaveBeenOrder(Long restaurantId);
}
