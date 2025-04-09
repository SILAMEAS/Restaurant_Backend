package com.sila.service;

import com.sila.dto.request.LoginRequest;
import com.sila.dto.response.AuthResponse;
import com.sila.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthService {
    ResponseEntity<String> signUp(User user);

    ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest req) throws Exception;

    ResponseEntity<AuthResponse> refreshToken(String refreshToken);
}
