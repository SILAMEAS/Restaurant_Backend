package com.sila.controller.admin;

import com.sila.dto.request.FoodRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Food Controller", description = "Admin operations related to food")
@RestController
@RequestMapping("api/admin/foods")
@RequiredArgsConstructor
public class AdminFoodController {

    private final FoodService foodService;
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final RestaurantRepository restaurantRepository;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<Food> createFood(
            @RequestHeader("Authorization") String jwt,
            @Valid @RequestBody FoodRequest req) throws Exception {

        userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantService.findRestaurantById(req.getRestaurantId());
        Category category = categoryService.findCategoryById(req.getCategoryId());
        Food food = foodService.createFood(req, category, restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) throws Exception {

        userService.findUserByJwtToken(jwt);
        foodService.deleteFoodById(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<String> deleteFoodByCategoryId(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long categoryId) throws Exception {

        userService.findUserByJwtToken(jwt);
        String result = foodService.deleteFoodByCategoryId(categoryId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<FoodResponse> updateFood(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long foodId,
            @Valid @RequestBody FoodRequest req) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        Food foodToUpdate = foodService.findFoodById(foodId);

        if (!foodToUpdate.getRestaurant().getId().equals(restaurant.getId())) {
            throw new BadRequestException("Food is not in this user's restaurant");
        }

        Food updatedFood = foodService.updateFood(req, foodId);
        FoodResponse response = modelMapper.map(updatedFood, FoodResponse.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/availability-status")
    public ResponseEntity<Food> updateAvailabilityStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) throws Exception {

        userService.findUserByJwtToken(jwt);
        Food updatedFood = foodService.updateAvailibilityStatus(id);

        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }
}
