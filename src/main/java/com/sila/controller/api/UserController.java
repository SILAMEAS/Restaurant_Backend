package com.sila.controller.api;

import com.sila.dto.request.UserReq;
import com.sila.dto.response.UserRes;
import com.sila.model.User;
import com.sila.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @GetMapping("/profile")
    public ResponseEntity<UserRes> getUserByJwtToken() throws Exception {
        return new ResponseEntity<>(userService.getUserProfile(), HttpStatus.OK);
    }
    @PutMapping("/profile")
    public ResponseEntity<UserRes> updateProfile(@RequestHeader("Authorization") String jwt,@Valid @RequestBody UserReq userReq) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(userService.updateProfile(user,userReq), HttpStatus.OK);
    }

}
