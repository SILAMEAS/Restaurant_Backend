package com.sila.service;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.SearchReq;
import com.sila.dto.request.UserReq;
import com.sila.dto.response.UserRes;
import com.sila.model.User;
import org.springframework.data.domain.Pageable;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;
    public Boolean findUserHasRoleAdmin(String jwt) throws Exception;
    public  User findUserByEmail(String email) throws Exception;
    public  User findUserById(Long userId);
    public EntityResponseHandler<UserRes> listUser(Pageable pageable, String search) throws Exception;

    UserRes updateProfile(User user, UserReq userReq) throws Exception;
}
