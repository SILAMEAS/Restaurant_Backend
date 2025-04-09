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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        userService.getByJwt(jwt);
        Restaurant restaurant = restaurantService.getById(req.getRestaurantId());
        Category category = categoryService.getById(req.getCategoryId());
        Food food = foodService.create(req, category, restaurant);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) throws Exception {

        userService.getByJwt(jwt);
        foodService.delete(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<String> deleteFoodByCategoryId(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long categoryId) throws Exception {

        userService.getByJwt(jwt);
        String result = foodService.deleteByCategoryId(categoryId);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{foodId}")
    public ResponseEntity<FoodResponse> updateFood(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long foodId,
            @Valid @RequestBody FoodRequest req) throws Exception {

        User user = userService.getByJwt(jwt);
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        Food foodToUpdate = foodService.getById(foodId);

        if (!foodToUpdate.getRestaurant().getId().equals(restaurant.getId())) {
            throw new BadRequestException("Food is not in this user's restaurant");
        }

        Food updatedFood = foodService.update(req, foodId);
        FoodResponse response = modelMapper.map(updatedFood, FoodResponse.class);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/availability-status")
    public ResponseEntity<Food> updateAvailabilityStatus(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long id) throws Exception {

        userService.getByJwt(jwt);
        Food updatedFood = foodService.updateStatus(id);

        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }
}
