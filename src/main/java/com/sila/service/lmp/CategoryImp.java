package com.sila.service.lmp;

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
import com.sila.utlis.context.UserContext;
import com.sila.utlis.enums.USER_ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
    private final FoodService foodService;
    private final FoodRepository foodRepository;

    @Override
    public Category createCategory(String jwt,String name) {
        User user = UserContext.getUser();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        if (categoryRepository.existsByNameAndRestaurant(name, restaurant)) {
            throw new BadRequestException("Category name already exists for this restaurant");
        }
        Category category =Category.builder().name(name).restaurant(restaurant).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category editCategory( String name, Long categoryId) {
        USER_ROLE user = UserContext.getUser().getRole();
        if(user.equals(USER_ROLE.ROLE_CUSTOMER)){
            throw new AccessDeniedException("Customer do not have permission to change category");
        }
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
    public Optional<Category> deleteCategoryById(Long categoryId) throws Exception {
        findCategoryById(categoryId);
        var isExitFoodInCategory = foodRepository.findAllByCategoryId(categoryId);
        if(!isExitFoodInCategory.isEmpty()){
            foodService.deleteFoodByCategoryId(categoryId);
        }
        categoryRepository.deleteById(categoryId);
        return null;
    }

}
