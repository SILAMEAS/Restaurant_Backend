package com.sila.service;
import com.sila.dto.request.LoginReq;
import com.sila.dto.response.AuthResponse;
import com.sila.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthService {
    ResponseEntity<String> register(User user);
    ResponseEntity<AuthResponse> login(@RequestBody LoginReq req) throws Exception;
}
