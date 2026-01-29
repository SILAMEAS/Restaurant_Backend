package com.sila.modules.profile.services;

import com.sila.modules.profile.dto.req.LoginRequest;
import com.sila.modules.profile.dto.req.SignUpRequest;
import com.sila.modules.profile.dto.res.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;


public interface AuthService {
    ResponseEntity<String> signUp(SignUpRequest request);


    ResponseEntity<LoginResponse> signIn(@RequestBody LoginRequest req);

    ResponseEntity<LoginResponse> refreshToken(String refreshToken);
}
