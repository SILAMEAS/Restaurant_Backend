package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.UserRequest;
import com.sila.dto.response.UserResponse;
import com.sila.model.User;
import com.sila.service.UserService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.common.PaginationDefaults;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Profile Controller", description = "Operations related to Profile")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserByJwtToken() throws Exception {
        return new ResponseEntity<>(userService.getProfile(), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateProfile(@ModelAttribute @Valid UserRequest userReq) throws Exception {
        return new ResponseEntity<>(userService.update(userReq), HttpStatus.OK);
    }
    @PreAuthorization({ROLE.ADMIN})
    @GetMapping
    public ResponseEntity<EntityResponseHandler<UserResponse>> listUsers(
            @RequestParam(defaultValue = PaginationDefaults.PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = PaginationDefaults.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = PaginationDefaults.SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationDefaults.SORT_ORDER) String sortOrder,
            @RequestParam(required = false) String search) throws Exception {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortBy));
        return new ResponseEntity<>(userService.list(pageable, search), HttpStatus.OK);
    }
}
