package com.sila.controller.admin;

import com.sila.dto.response.FoodRes;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.dto.request.FoodReq;
import com.sila.model.User;
import com.sila.repository.FoodRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/admin/foods")
@RequiredArgsConstructor
public class AdminFoodController {
    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    @PostMapping()
    public ResponseEntity<Food> createFood( @RequestHeader("Authorization") String jwt,@Valid @RequestBody FoodReq req) throws Exception {
        userService.findUserByJwtToken(jwt);
        Restaurant restaurant=restaurantService.findRestaurantById(req.getRestaurantId());
        Category category=categoryService.findCategoryById(req.getCategoryId());
        Food food=foodService.createFood(req,category,restaurant);
        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        userService.findUserByJwtToken(jwt);
        foodService.deleteFoodById(id);
        return new ResponseEntity<>("Successfully Delete",HttpStatus.OK);
    }
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<String> deleteFoodByCategoryId(@RequestHeader("Authorization") String jwt, @PathVariable Long categoryId ) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(foodService.deleteFoodByCategoryId(categoryId),HttpStatus.OK);
    }
    @PutMapping("/{foodId}")
    public ResponseEntity<FoodRes> updateFood(@RequestHeader("Authorization") String jwt, @PathVariable Long foodId,@Valid @RequestBody FoodReq req) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        Food foodUpdate = foodService.findFoodById(foodId);
        if(!foodUpdate.getRestaurant().getId().equals(restaurant.getId())){
            throw  new BadRequestException("Food is not in this user's restaurant");
        }
        Food food = foodService.updateFood(req,foodId);
        FoodRes foodRes=this.modelMapper.map(food,FoodRes.class);
        return new ResponseEntity<>(foodRes,HttpStatus.OK);
    }
    @PutMapping("/{id}/avaibility-status")
    public ResponseEntity<Food> updateAvailibilityStatus(@RequestHeader("Authorization") String jwt, @PathVariable Long id) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(foodService.updateAvailibilityStatus(id),HttpStatus.OK);
    }
}
