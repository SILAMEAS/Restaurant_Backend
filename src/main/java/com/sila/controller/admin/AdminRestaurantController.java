package com.sila.controller.admin;

import com.sila.dto.response.RestaurantRes;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.RestaurantRepository;
import com.sila.dto.request.RestaurantReq;
import com.sila.dto.response.MessageResponse;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/restaurants")
@RequiredArgsConstructor
@Slf4j
public class AdminRestaurantController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final RestaurantRepository restaurantRepository;
    private final ModelMapper modelMapper;
    @GetMapping("/get")
    public ResponseEntity<String> findDD(){
        return new ResponseEntity<>("Can access to /api/admin/restaurant/get",HttpStatus.OK);
    }
    @PostMapping()
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody RestaurantReq req, @RequestHeader("Authorization") String jwt) throws Exception {
        return new ResponseEntity<>(restaurantService.createRestaurant(req,jwt), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantRes> updateRestaurant(@RequestBody RestaurantReq restaurantReq, @RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        User findUser = userService.findUserByJwtToken(jwt);
        Optional<Restaurant> findRestaurantById = restaurantRepository.findById(id);
        Restaurant findRestaurantByOwnerId =  restaurantRepository.findByOwnerId(findUser.getId());
        boolean RestaurantBelongToUser= Objects.equals(findRestaurantById.get().getOwner(), findRestaurantByOwnerId.getOwner());
        if(RestaurantBelongToUser){
            restaurantReq.setId(id);
            Restaurant restaurant= restaurantService.updateRestaurant(restaurantReq);
            RestaurantRes restaurantRes=this.modelMapper.map(restaurant,RestaurantRes.class);
            return new ResponseEntity<>(restaurantRes, HttpStatus.OK);
        }
        throw  new BadRequestException("This user isn't owner of restaurant");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRestaurant(@RequestHeader("Authorization") String jwt,@PathVariable Long id) throws Exception {
        userService.findUserByJwtToken(jwt);
        MessageResponse messageResponse=new MessageResponse();
        restaurantService.deleteRestaurant(id);
        messageResponse.setMessage("delete restaurant id : "+id+" successfully!");
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateRestaurantStatus(@RequestHeader("Authorization") String jwt,@PathVariable Long id) throws Exception {
        userService.findUserByJwtToken(jwt);
        Restaurant restaurant= restaurantService.updateRestaurantStatus(id);
        return new ResponseEntity<>(restaurant.isOpen()? "Restaurant is open!":"Restaurant is close!", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<Restaurant> findRestaurantByUserid(@RequestHeader("Authorization") String jwt) throws Exception {
        User findUser = userService.findUserByJwtToken(jwt);
        Restaurant restaurant= restaurantService.getRestaurantByUserId(findUser.getId());
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

}
