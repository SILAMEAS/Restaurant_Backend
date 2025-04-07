package com.sila.dto.request;

import com.sila.model.Category;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class FoodRequest {
    private String name;
    private String description;
    private Long price;
    private Category category;
    @NotEmpty
    private List<String> images;
    private Long restaurantId;
    private Long categoryId;
    private boolean vegetarian;
    private boolean seasonal;
    private boolean available;
}
