package com.sila.controller;

import com.sila.config.context.UserContext;
import com.sila.repository.CategoryRepository;
import com.sila.service.FoodService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard")
@RequiredArgsConstructor
public class DashboardController {
    final UserService userService;
    final CategoryRepository categoryRepository;
    final RestaurantService restaurantService;
    private final FoodService foodService;

    @PreAuthorization({ROLE.OWNER,ROLE.ADMIN})
    @GetMapping
    public ResponseEntity<Object> getAdminDashboard() throws Exception {
        Map<String, Object> res = new HashMap<>();
        if(UserContext.getUser().getRole().equals(ROLE.OWNER)){
            var restaurantId= restaurantService.getByUserLogin().getId();
            res.put("total_users",userService.allHaveBeenOrder( restaurantId));
            res.put("total_foods", foodService.all(restaurantId));
        }else {
            res.put("total_users",userService.all());
            res.put("total_foods", foodService.all());
        }
        res.put("total_orders", 0);
        res.put("total_categories",categoryRepository.count());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    private Map<String, Object> createCard(String id, String title, long value) {
        Map<String, Object> card = new HashMap<>();
        card.put("id", id);
        card.put("title", title);
        card.put("value", value);
        return card;
    }
}
