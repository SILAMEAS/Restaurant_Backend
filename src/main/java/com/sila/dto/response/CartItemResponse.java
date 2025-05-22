package com.sila.dto.response;

import com.sila.model.Cart;
import com.sila.model.CartItem;
import com.sila.model.Food;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartItemResponse {
    private Long id;
    private int quantity;
    private FoodResponse food;
}
