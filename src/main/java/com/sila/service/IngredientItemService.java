package com.sila.service;

import com.sila.model.IngredientsItem;

import java.util.List;

public interface IngredientItemService {
    public IngredientsItem createIngredientItem(Long restaurant_id,String ingredient_item_name,Long category_id)throws Exception;
    public List<IngredientsItem> findIngredientItemsByRestaurantId(Long restaurant_id)throws Exception;
    public IngredientsItem updateIngredientItemStock(Long ingredient_id)throws Exception;
}
