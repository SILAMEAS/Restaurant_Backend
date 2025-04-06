package com.sila.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodByRestaurantIdRequest {
    @NotEmpty
    Long restaurantId;
    @NotEmpty
    boolean isVegin;
    @NotEmpty
    boolean isNonveg;
    @NotEmpty
    boolean isSeasional;
    @NotEmpty
    String foodCategory;
}
