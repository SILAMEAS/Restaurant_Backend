package com.sila.lmp;

import com.sila.model.IngredientCategory;
import com.sila.model.IngredientsItem;
import com.sila.model.Restaurant;
import com.sila.repository.IngredientItemRepository;
import com.sila.service.CategoryService;
import com.sila.service.IngredientCategoryService;
import com.sila.service.IngredientItemService;
import com.sila.service.RestaurantService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IngredientItemImp implements IngredientItemService {
    private final IngredientItemRepository ingredientItemRepository;
    private final IngredientCategoryService ingredientCategoryService;
    private final RestaurantService restaurantService;
    private final CategoryService categoryService;
    @Override
    public IngredientsItem createIngredientItem(Long restaurant_id, String ingredient_item_name, Long category_id) throws Exception {
        Restaurant restaurant=restaurantService.findRestaurantById(restaurant_id);
        IngredientCategory category = ingredientCategoryService.findIngredientCategoryById(category_id);
        IngredientsItem tmp_ingredient=new IngredientsItem();
        tmp_ingredient.setCategory(category);
        tmp_ingredient.setName(ingredient_item_name);
        tmp_ingredient.setRestaurant(restaurant);
        tmp_ingredient.setInStock(true);
        IngredientsItem save_ingredient=ingredientItemRepository.save(tmp_ingredient);
        category.getIngredientsItems().add(save_ingredient);
        return tmp_ingredient;
    }

    @Override
    public List<IngredientsItem> findIngredientItemsByRestaurantId(Long restaurant_id) throws Exception {
        return ingredientItemRepository.findByRestaurantId(restaurant_id);
    }

    @Override
    public IngredientsItem updateIngredientItemStock(Long ingredient_id) throws Exception {
        Optional<IngredientsItem> Option_Ingredients=ingredientItemRepository.findById(ingredient_id);
        if(Option_Ingredients.isEmpty()){
            throw new BadRequestException("ingredients not found!");
        }
        IngredientsItem ingredients = Option_Ingredients.get();
        ingredients.setInStock(!ingredients.isInStock());
        return ingredientItemRepository.save(ingredients);
    }
}
