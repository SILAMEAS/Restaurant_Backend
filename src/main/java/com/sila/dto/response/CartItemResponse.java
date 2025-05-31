package com.sila.dto.response;

import com.sila.model.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class CartItemResponse {
    private Long id;
    private int quantity;
    private FoodResponse food;

    public static CartItemResponse toResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .id(cartItem.getId())
                .food(FoodResponse.toResponse(cartItem.getFood()))
                .quantity(cartItem.getQuantity())
                .build();
    }
}
