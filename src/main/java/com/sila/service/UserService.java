package com.sila.service;

import com.sila.model.User;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;
    public  User findUserByEmail(String email) throws Exception;
    public  User findUserById(Long userId);
}
