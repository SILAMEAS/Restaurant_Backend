package com.sila.lmp;

import com.sila.model.IngredientCategory;
import com.sila.model.Restaurant;
import com.sila.repository.IngredientCategoryRepository;
import com.sila.service.IngredientCategoryService;
import com.sila.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class IngredientCategoryImp implements IngredientCategoryService {
    private final RestaurantService restaurantService;
    private final IngredientCategoryRepository ingredientCategoryRepository;
    @Override
    public IngredientCategory createIngredientCategory(String name_ingredient_category, Long restaurant_id) throws Exception {
        Restaurant restaurant = restaurantService.findRestaurantById(restaurant_id);
        IngredientCategory ingredientCategory=new IngredientCategory();
        ingredientCategory.setName(name_ingredient_category);
        ingredientCategory.setRestaurant(restaurant);
        return ingredientCategoryRepository.save(ingredientCategory);
    }

    @Override
    public IngredientCategory findIngredientCategoryById(Long category_id) throws Exception {
        Optional<IngredientCategory> isExit= ingredientCategoryRepository.findById(category_id);
        if(isExit.isEmpty()){
            throw  new BadRequestException("IngredientCategory id :"+category_id+" is not found");
        }
        return isExit.get();
    }

    @Override
    public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restaurant_id) throws Exception {
        return ingredientCategoryRepository.findByRestaurantId(restaurant_id);
    }
}
