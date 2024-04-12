package com.sila.service;

import com.sila.model.IngredientCategory;

import java.util.List;

public interface IngredientCategoryService {
    public IngredientCategory createIngredientCategory(String name_ingredient_category,Long restaurant_id)throws Exception;
    public IngredientCategory findIngredientCategoryById(Long category_id)throws Exception;
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurant_id)throws Exception;
}
