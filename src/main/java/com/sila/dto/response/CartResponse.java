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
    private String restaurantName;
    private Long totalItems;

    public static CartResponse toResponse(Cart cart) {
        var items =cart.getItems().stream().map(CartItemResponse::toResponse).toList();
        String name = items.stream()
                .findFirst()
                .map(item -> item.getFood())
                .map(food -> food.getRestaurantName())
                .orElse("UNKONWN");

        return CartResponse.builder()
                .id(cart.getId())
                .totalItems((long) cart.getItems().size())
                .restaurantName(name)
                .items(items)
                .build();
    }
}
