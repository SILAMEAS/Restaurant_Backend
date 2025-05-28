package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.request.UserRequest;
import com.sila.dto.response.AddressResponse;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.UserResponse;
import com.sila.service.AddressService;
import com.sila.service.FavoriteService;
import com.sila.service.UserService;
import com.sila.util.PageableUtil;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Profile Controller", description = "Operations related to Profile")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final AddressService addressService;

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
            @ModelAttribute PaginationRequest request
    ) throws Exception {
        Pageable pageable = PageableUtil.fromRequest(request);
        return new ResponseEntity<>(userService.list(pageable, request.getSearch()), HttpStatus.OK);
    }
    @PreAuthorization({ROLE.OWNER})
    @GetMapping("/{restaurantId}/user-orders")
    public ResponseEntity<EntityResponseHandler<UserResponse.UserResponseCustom>> listUsersHasOrderInRestaurant(
            @PathVariable Long restaurantId,
            @ModelAttribute PaginationRequest request
    )  {
        Pageable pageable = PageableUtil.fromRequest(request);
        return new ResponseEntity<>(userService.getUsersWhoOrderedFromRestaurant(restaurantId, pageable), HttpStatus.OK);
    }
    @GetMapping("/address")
    public ResponseEntity<List<AddressResponse>> getMyAddress()  {
        return new ResponseEntity<>(addressService.getByUser(), HttpStatus.OK);
    }
    @GetMapping("/favorite")
    public ResponseEntity<List<FavoriteResponse>> getMyFav()  {
        return new ResponseEntity<>(favoriteService.getMyFav(), HttpStatus.OK);
    }
}
