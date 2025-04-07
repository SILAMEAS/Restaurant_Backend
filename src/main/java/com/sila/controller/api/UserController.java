package com.sila.controller.api;

import com.sila.dto.request.UserRequest;
import com.sila.dto.response.UserResponse;
import com.sila.model.User;
import com.sila.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Profile Controller", description = "Operations related to Profile")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserByJwtToken() throws Exception {
        return new ResponseEntity<>(userService.getUserProfile(), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@RequestHeader("Authorization") String jwt, @Valid @RequestBody UserRequest userReq) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(userService.updateProfile(user, userReq), HttpStatus.OK);
    }

}
