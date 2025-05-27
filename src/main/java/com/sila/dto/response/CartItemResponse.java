package com.sila.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItemResponse {
    private Long id;
    private int quantity;
    private FoodResponse food;
}
