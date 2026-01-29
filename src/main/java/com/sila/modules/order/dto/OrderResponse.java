package com.sila.modules.order.dto;

import com.sila.modules.profile.dto.res.UserResponse;
import com.sila.modules.resturant.dto.RestaurantResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private UserResponse user;
    private RestaurantResponse restaurant;
    private double totalAmount;
    private LocalDateTime createdAt;
    private String status;
    private List<OrderItemResponse> items;

    // Getters and setters
}