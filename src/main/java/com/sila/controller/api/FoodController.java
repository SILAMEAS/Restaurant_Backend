package com.sila.controller.api;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FoodResponse;
import com.sila.service.FoodService;
import com.sila.service.UserService;
import com.sila.util.common.PaginationDefaults;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Food Controller", description = "User operations related to Food")
@RestController
@RequestMapping("api/foods")
@RequiredArgsConstructor
@Slf4j
public class FoodController {
    private final FoodService foodService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<EntityResponseHandler<FoodResponse>> listFoods(@RequestHeader("Authorization") String jwt, @RequestParam(required = false) boolean vegetarian, @RequestParam(required = false) boolean seasanal, @RequestParam(required = false) String filterBy, @RequestParam(required = false) String search, @RequestParam(defaultValue = PaginationDefaults.PAGE_NO) Integer pageNo, @RequestParam(defaultValue = PaginationDefaults.PAGE_SIZE) Integer pageSize, @RequestParam(defaultValue = PaginationDefaults.SORT_BY) String sortBy, @RequestParam(defaultValue = PaginationDefaults.SORT_ORDER) String sortOrder) throws Exception {

        userService.getByJwt(jwt);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortBy));
        var foodResEntityResponseHandler = foodService.gets(pageable, new SearchRequest(search, seasanal, vegetarian), filterBy);
        return new ResponseEntity<>(foodResEntityResponseHandler, HttpStatus.OK);
    }

    @GetMapping("restaurant/{restaurantId}")
    public ResponseEntity<EntityResponseHandler<FoodResponse>> listFoodByRestaurantId(@RequestHeader("Authorization") String jwt, @RequestParam(required = false) boolean vegetarian, @RequestParam(required = false) boolean seasanal, @RequestParam(required = false) String filterBy, @RequestParam(required = false) String search, @RequestParam(defaultValue = PaginationDefaults.PAGE_NO) Integer pageNo, @RequestParam(defaultValue = PaginationDefaults.PAGE_SIZE) Integer pageSize, @RequestParam(defaultValue = PaginationDefaults.SORT_BY) String sortBy, @RequestParam(defaultValue = PaginationDefaults.SORT_ORDER) String sortOrder, @PathVariable Long restaurantId) throws Exception {

        userService.getByJwt(jwt);
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()), sortBy));
        var foodResEntityResponseHandler = foodService.getsByResId(restaurantId, pageable, new SearchRequest(search, seasanal, vegetarian), filterBy);
        return new ResponseEntity<>(foodResEntityResponseHandler, HttpStatus.OK);
    }
}
