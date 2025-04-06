package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.SearchReq;
import com.sila.dto.request.UserReq;
import com.sila.dto.response.UserRes;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findUserByJwtToken(String jwt) throws Exception;
    Boolean findUserHasRoleAdmin(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
    User findUserById(Long userId);
    EntityResponseHandler<UserRes> listUser(Pageable pageable, String search) throws Exception;

    UserRes updateProfile(User user, UserReq userReq) throws Exception;
    UserRes getUserProfile() throws Exception;
}
