package com.sila.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
}
