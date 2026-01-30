package com.sila.modules.dashboard.services;

import com.sila.config.context.UserContext;
import com.sila.modules.category.services.CategoryService;
import com.sila.modules.dashboard.dto.res.DashboardResponse;
import com.sila.modules.food.services.FoodService;
import com.sila.modules.profile.services.UserService;
import com.sila.modules.resturant.services.RestaurantService;
import com.sila.share.enums.ROLE;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImp implements DashboardService {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final FoodService foodService;
    private final CategoryService categoryService;

    @Override
    public DashboardResponse overviews() {
//        Create new Response
        var dashboardResponse = new DashboardResponse();
//        Check if role equal OWNER
        if (UserContext.getUser().getRole().equals(ROLE.OWNER)) {
            var restaurantId = restaurantService.getByUserLogin().getId();
            dashboardResponse.setTotal_users(userService.countById(restaurantId));
            dashboardResponse.setTotal_foods(foodService.count(restaurantId));
        } else {
            dashboardResponse.setTotal_users(userService.count());
            dashboardResponse.setTotal_foods(foodService.count());
            dashboardResponse.setTotal_restaurants(restaurantService.count());

        }
        dashboardResponse.setTotal_orders(0L);
        dashboardResponse.setTotal_categories(categoryService.count());
        return dashboardResponse;
    }
}
