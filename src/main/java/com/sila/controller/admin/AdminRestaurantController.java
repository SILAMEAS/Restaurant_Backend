package com.sila.controller.admin;

import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.response.MessageResponse;
import com.sila.dto.response.RestaurantResponse;
import com.sila.model.Restaurant;
import com.sila.service.RestaurantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Restaurant Controller", description = "Admin operations related to Restaurant")
@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
@Slf4j
public class AdminRestaurantController {
    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    @GetMapping("/get")
    public ResponseEntity<String> findDD() {
        return new ResponseEntity<>("Can access to /api/admin/restaurant/get", HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody RestaurantRequest restaurantReq) throws Exception {
        return new ResponseEntity<>(restaurantService.createRestaurant(restaurantReq), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> updateRestaurant(@RequestBody RestaurantRequest restaurantReq, @PathVariable Long id) throws Exception {
        Restaurant restaurant = restaurantService.updateRestaurant(restaurantReq, id);
        return new ResponseEntity<>(this.modelMapper.map(restaurant, RestaurantResponse.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@PathVariable Long id) throws Exception {
        MessageResponse messageResponse = new MessageResponse();
        restaurantService.deleteRestaurant(id);
        messageResponse.setMessage("delete restaurant id : " + id + " successfully!");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateRestaurantStatus(@PathVariable Long id) throws Exception {
        Restaurant restaurant = restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(restaurant.isOpen() ? "Restaurant is open!" : "Restaurant is close!", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserid() throws Exception {
        Restaurant restaurant = restaurantService.getRestaurantByUserId();
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
