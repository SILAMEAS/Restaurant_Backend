package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.method.OnCreate;
import com.sila.dto.method.OnUpdate;
import com.sila.dto.request.RestaurantRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.FavoriteResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.dto.response.RestaurantResponse;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.sila.service.lmp.RestaurantImp.mapToRestaurantResponse;

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
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(pageNo-1, pageSize,Sort.by(Direction.valueOf(sortOrder.toUpperCase()),sortBy));
        SearchRequest searchReq=new SearchRequest();
        searchReq.setSearch(search);
        return new ResponseEntity<>(restaurantService.search(pageable,searchReq),HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getRestaurantById(@PathVariable Long id) throws Exception {
        return new ResponseEntity<>(mapToRestaurantResponse(restaurantService.getById(id)), HttpStatus.OK);
    }
    @PutMapping("/{id}/favorites")
    public ResponseEntity<List<FavoriteResponse>> addRestaurantToFavorites( @PathVariable Long id) {
        return new ResponseEntity<>(restaurantService.addFav(id), HttpStatus.OK);
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

