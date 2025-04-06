package com.sila.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Long cardItemId;
    private int qty;

}
