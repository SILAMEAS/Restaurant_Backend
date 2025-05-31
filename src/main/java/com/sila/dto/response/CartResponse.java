package com.sila.dto.response;

import com.sila.model.Cart;
import com.sila.model.CartItem;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;

    public static CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .items(cart.getItems().stream().map(CartItemResponse::toResponse).toList())
                .build();
    }
}
