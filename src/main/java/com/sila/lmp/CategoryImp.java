package com.sila.lmp;

import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
    private final FoodService foodService;
    private final FoodRepository foodRepository;
    @Override
    public Category createCategory(String jwt,String name) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        Category category = new Category();
        category.setName(name);
        category.setRestaurant(restaurant);
        return categoryRepository.save(category);
    }

    @Override
    public Category editCategory(String jwt, String name, Long categoryId) throws Exception {
        userService.findUserByJwtToken(jwt);
        Category category = findCategoryById(categoryId);
        if(!name.isEmpty()){
            category.setName(name);
        }
        return categoryRepository.save(category);
    }
    public Category findCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).orElseThrow(()->new BadRequestException("category not found"));
    }
    @Override
    public List<Category> listCategoriesByRestaurantId(Long restaurant_id) {
        return categoryRepository.findByRestaurantId(restaurant_id);
    }
    @Override
    public void deleteCategoryById(Long category_id) throws Exception {
        findCategoryById(category_id);
        var isExitFoodInCategory = foodRepository.findAllByCategoryId(category_id);
        if(!isExitFoodInCategory.isEmpty()){
            foodService.deleteFoodByCategoryId(category_id);
        }
        categoryRepository.deleteById(category_id);
    }

}
