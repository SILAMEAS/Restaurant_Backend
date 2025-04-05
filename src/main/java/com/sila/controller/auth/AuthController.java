package com.sila.controller.auth;
import com.sila.dto.request.LoginReq;
import com.sila.dto.response.AuthResponse;
import com.sila.model.User;
import com.sila.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        return authService.register(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginReq loginReq) throws Exception {
        return authService.login(loginReq);
    }
}
