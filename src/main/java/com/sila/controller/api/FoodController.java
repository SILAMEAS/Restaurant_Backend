package com.sila.controller.api;

import com.sila.dto.entityResponseHandler.EntityResponseHandler;
import com.sila.dto.request.SearchReq;
import com.sila.dto.response.FoodRes;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/foods")
@RequiredArgsConstructor
@Slf4j
public class FoodController {

  private final FoodService foodService;
  private final UserService userService;
  private final RestaurantService restaurantService;
  private final ModelMapper modelMapper;
@GetMapping("")
public ResponseEntity<EntityResponseHandler<FoodRes>> listFoods(
        @RequestHeader("Authorization") String jwt,
        @RequestParam(required = false) boolean vegetarian,
        @RequestParam(required = false) boolean seasanal,
        @RequestParam(required = false) String filterBy,
        @RequestParam(required = false) String search,
        @RequestParam(defaultValue = "1") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy,
        @RequestParam(defaultValue = "desc") String sortOrder)
        throws Exception {

        userService.findUserByJwtToken(jwt);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()),sortBy));
        var foodResEntityResponseHandler=foodService.listFoods(pageable, new SearchReq(search,seasanal,vegetarian), filterBy);
        return new ResponseEntity<>(foodResEntityResponseHandler,
                HttpStatus.OK);
}
  @GetMapping("restaurant/{restaurantId}")
  public ResponseEntity<EntityResponseHandler<FoodRes>> listFoodByRestaurantId(
          @RequestHeader("Authorization") String jwt,
          @RequestParam(required = false) boolean vegetarian,
          @RequestParam(required = false) boolean seasanal,
          @RequestParam(required = false) String filterBy,
          @RequestParam(required = false) String search,
          @RequestParam(defaultValue = "1") Integer pageNo,
          @RequestParam(defaultValue = "10") Integer pageSize,
          @RequestParam(defaultValue = "id") String sortBy,
          @RequestParam(defaultValue = "desc") String sortOrder,
          @PathVariable Long restaurantId)
          throws Exception {

    userService.findUserByJwtToken(jwt);
    Pageable pageable = PageRequest.of(pageNo-1, pageSize,Sort.by(Sort.Direction.valueOf(sortOrder.toUpperCase()),sortBy));
    var foodResEntityResponseHandler=foodService.listFoodsByRestaurantId(restaurantId,pageable, new SearchReq(search,seasanal,vegetarian),filterBy);
    return new ResponseEntity<>(foodResEntityResponseHandler,
            HttpStatus.OK);
  }
}
