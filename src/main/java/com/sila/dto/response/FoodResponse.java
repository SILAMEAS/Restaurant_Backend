package com.sila.dto.response;

import com.sila.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class FoodResponse implements Serializable {
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private Long price;
    private List<String> images;
    private Long restaurantId;
    private transient Category category;
    private boolean isVegetarian;
    private boolean isSeasonal;
    private boolean available;
}
