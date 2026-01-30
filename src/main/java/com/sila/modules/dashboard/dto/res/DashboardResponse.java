package com.sila.modules.dashboard.dto.res;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DashboardResponse {
    Long total_users;
    Long total_foods;
    Long total_restaurants;
    Long total_orders;
    Long total_categories;

}
