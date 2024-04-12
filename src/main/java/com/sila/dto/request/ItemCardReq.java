package com.sila.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ItemCardReq {
    private Long foodId;
    private Integer qty;
    private List<String> ingredients;
    private Long restaurantId;

}
