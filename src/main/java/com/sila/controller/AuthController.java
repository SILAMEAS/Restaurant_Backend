package com.sila.controller;

import com.sila.dto.request.LoginRequest;
import com.sila.dto.request.SignUpRequest;
import com.sila.dto.response.AuthResponse;
import com.sila.model.User;
import com.sila.service.AuthService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "Auth Controller", description = "Operations related to Auth")
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignUpRequest request) {

        return authService.signUp(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginReq) {
        return authService.signIn(loginReq);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        return authService.refreshToken(refreshToken);
    }

    @Hidden
    @GetMapping("/test-api")
    public ResponseEntity<String> getTest() {
        return new ResponseEntity<>("Api working", HttpStatus.OK);
    }
}
