package com.sila.modules.resturant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class DiscountResponse {
    private double total;
    private double restaurant;
    private double food;
}
