package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.UserRequest;
import com.sila.dto.response.UserResponse;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;
    Boolean findUserHasRoleAdmin(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
    User findUserById(Long userId);
    EntityResponseHandler<UserResponse> listUser(Pageable pageable, String search) throws Exception;

    UserResponse updateProfile(User user, UserRequest userReq) throws Exception;
    UserResponse getUserProfile() throws Exception;
}
