package com.sila.controller.api;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.SearchReq;
import com.sila.dto.response.RestaurantRes;
import com.sila.model.User;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @GetMapping("")
    public ResponseEntity<EntityResponseHandler<RestaurantRes>> searchRestaurants(@RequestHeader("Authorization") String jwt,
        @RequestParam(defaultValue = "1") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "ASC") String sortOrder,
        @RequestParam(required = false) String search,
        @RequestParam(required = false,defaultValue = "true") Boolean sessional,
        @RequestParam(required = false,defaultValue = "true") Boolean vegetarian) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,Sort.by(Direction.valueOf(sortOrder.toUpperCase()),sortBy));
        SearchReq searchReq=new SearchReq();
        searchReq.setSearch(search);
        searchReq.setSessional(sessional);
        searchReq.setVegeterain(vegetarian);
        return new ResponseEntity<>(restaurantService.searchRestaurant(pageable,searchReq),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantRes> getRestaurantById(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        userService.findUserByJwtToken(jwt);
        var restaurant = this.modelMapper.map(restaurantService.findRestaurantById(id), RestaurantRes.class);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
    @PutMapping("/{id}/add-favorites")
    public ResponseEntity<User> addRestaurantToFavorites(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        User userLogin=userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(restaurantService.addRestaurantToFavorites(id,userLogin), HttpStatus.OK);
    }
}

