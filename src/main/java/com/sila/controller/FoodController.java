package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.method.OnCreate;
import com.sila.dto.method.OnUpdate;
import com.sila.dto.request.FoodRequest;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.model.Category;
import com.sila.model.Food;
import com.sila.model.Restaurant;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.common.PaginationDefaults;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Food Controller")
@RestController
@RequestMapping("api/foods")
@RequiredArgsConstructor
@Slf4j
public class FoodController {
    private final FoodService foodService;
    private final ModelMapper modelMapper;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<EntityResponseHandler<FoodResponse>> listFoods(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(foodService.gets(request));
    }

    @PreAuthorization({ROLE.OWNER})
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createFood(
            @Validated(OnCreate.class) @ModelAttribute FoodRequest foodRequest,
            @RequestParam("images") List<MultipartFile> imageFiles) throws Exception {

        Restaurant restaurant = restaurantService.getById(foodRequest.getRestaurantId());
        Category category = categoryService.getById(foodRequest.getCategoryId());
        foodService.create(foodRequest, category, restaurant, imageFiles);

        return new ResponseEntity<>("Food Created", HttpStatus.CREATED);
    }
    @PreAuthorization({ROLE.OWNER})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(
            @PathVariable Long id) {

        foodService.delete(id);

        return new ResponseEntity<>("Successfully deleted", HttpStatus.OK);
    }

    @GetMapping("restaurant/{restaurantId}")
    public ResponseEntity<EntityResponseHandler<FoodResponse>> listFoodByRestaurantId( @RequestParam(required = false) boolean vegetarian, @RequestParam(required = false) boolean seasanal, @RequestParam(required = false) String filterBy, @RequestParam(required = false) String search, @RequestParam(defaultValue = PaginationDefaults.PAGE_NO) Integer pageNo, @RequestParam(defaultValue = PaginationDefaults.PAGE_SIZE) Integer pageSize, @RequestParam(defaultValue = PaginationDefaults.SORT_BY) String sortBy, @RequestParam(defaultValue = PaginationDefaults.SORT_ORDER) String sortOrder, @PathVariable Long restaurantId) throws Exception {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortBy));
        var foodResEntityResponseHandler = foodService.getsByResId(restaurantId, pageable, new SearchRequest(search, seasanal, vegetarian), filterBy);
        return new ResponseEntity<>(foodResEntityResponseHandler, HttpStatus.OK);
    }
    @PreAuthorization({ROLE.OWNER,ROLE.ADMIN})
    @PutMapping("/{foodId}")
    public ResponseEntity<FoodResponse> updateFood(
            @PathVariable Long foodId,
            @Validated(OnUpdate.class) @ModelAttribute FoodRequest foodRequest)  {

        Food updatedFood = foodService.update(foodRequest, foodId);

        return new ResponseEntity<>(modelMapper.map(updatedFood, FoodResponse.class), HttpStatus.OK);
    }

    @PreAuthorization({ROLE.ADMIN,ROLE.OWNER})
    @PutMapping("/{id}/availability-status")
    public ResponseEntity<Food> updateAvailabilityStatus(
            @PathVariable Long id)  {
        Food updatedFood = foodService.updateStatus(id);

        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }
}
