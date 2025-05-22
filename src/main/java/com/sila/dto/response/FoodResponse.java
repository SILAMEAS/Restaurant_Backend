package com.sila.dto.response;

import com.sila.model.Category;
import com.sila.model.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FoodResponse implements Serializable {
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private Long price;
    private List<String> images;
    private Long restaurantId;
    private transient CategoryDTO category;
    private boolean isVegetarian;
    private boolean isSeasonal;
    private boolean available;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDTO {
        private Long id;
        private String name;
    }

    public static FoodResponse toResponse(Food food) {
        return FoodResponse.builder()
                .id(food.getId())
                .name(food.getName())
                .description(food.getDescription())
                .price(food.getPrice())
                .category(new FoodResponse.CategoryDTO(
                        food.getCategory().getId(),
                        food.getCategory().getName()))
                .images(food.getImages().stream()
                        .map(img -> img.getUrl()) // assuming `getUrl()` exists in ImageFood
                        .toList())
                .available(food.isAvailable())
                .restaurantId(food.getRestaurant().getId()) // optional simplification
                .isVegetarian(food.isVegetarian())
                .isSeasonal(food.isSeasonal())
                .build();
    }
}


