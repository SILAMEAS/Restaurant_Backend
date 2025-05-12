package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.method.OnCreate;
import com.sila.dto.method.OnUpdate;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.common.PaginationDefaults;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Restaurant Controller")
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @GetMapping
    public ResponseEntity<EntityResponseHandler<RestaurantResponse>> searchRestaurants(
            @RequestParam(defaultValue = PaginationDefaults.PAGE_NO) Integer pageNo,
            @RequestParam(defaultValue = PaginationDefaults.PAGE_SIZE) Integer pageSize,
            @RequestParam(defaultValue = PaginationDefaults.SORT_BY) String sortBy,
            @RequestParam(defaultValue = PaginationDefaults.SORT_ORDER) String sortOrder,
            @RequestParam(required = false) String search,
            @RequestParam(required = false,defaultValue = "true") Boolean sessional,
            @RequestParam(required = false,defaultValue = "true") Boolean vegetarian) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,Sort.by(Direction.valueOf(sortOrder.toUpperCase()),sortBy));
        SearchRequest searchReq=new SearchRequest();
        searchReq.setSearch(search);
        searchReq.setSessional(sessional);
        searchReq.setVegeterain(vegetarian);
        return new ResponseEntity<>(restaurantService.search(pageable,searchReq),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        userService.getByJwt(jwt);
        var restaurant = this.modelMapper.map(restaurantService.getById(id), RestaurantResponse.class);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @PutMapping("/{id}/add-favorites")
    public ResponseEntity<List<FavoriteResponse>> addRestaurantToFavorites(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        User userLogin=userService.getByJwt(jwt);
        return new ResponseEntity<>(restaurantService.addFav(id,userLogin), HttpStatus.OK);
    }
    @PreAuthorization({ROLE.OWNER})
    @PostMapping()
    public ResponseEntity<RestaurantResponse> createRestaurant(
            @Validated(OnCreate.class) @ModelAttribute RestaurantRequest restaurantReq) {
        return new ResponseEntity<>(restaurantService.create(restaurantReq), HttpStatus.CREATED);
    }
    @PreAuthorization({ROLE.OWNER})
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(
            @Validated(OnUpdate.class) @ModelAttribute RestaurantRequest restaurantReq,
            @PathVariable Long id) throws Exception {
        return new ResponseEntity<>(restaurantService.update(restaurantReq, id), HttpStatus.OK);
    }
    @PreAuthorization({ROLE.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@PathVariable @Valid Long id) throws Exception {
        return new ResponseEntity<>(restaurantService.delete(id), HttpStatus.OK);
    }

    @PreAuthorization({ROLE.ADMIN,ROLE.OWNER})
    @GetMapping("/owner")
    public ResponseEntity<RestaurantResponse> findRestaurantByOwnerLogin() {
        return new ResponseEntity<>(this.modelMapper.map(restaurantService.getByUserLogin(), RestaurantResponse.class), HttpStatus.OK);
    }
}

