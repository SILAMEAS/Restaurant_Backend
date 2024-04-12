package com.sila.lmp;

import com.sila.dto.request.CategoryReq;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.CategoryRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.repository.UserRepository;
import com.sila.service.CategoryService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserService userService;
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
    public List<Category> listCategoriesByRestaurantId(Long restaurant_id) {
        return categoryRepository.findByRestaurantId(restaurant_id);
    }

    @Override
    public Category findCategoryById(Long category_id) throws Exception {
        Optional<Category> categoryExit= categoryRepository.findById(category_id);
        if(categoryExit.isEmpty()){
            throw new BadRequestException("Category id: "+category_id+" not found in database");
        }
        return categoryExit.get();
    }

    @Override
    public Void deleteCategoryById(Long category_id) throws Exception {
        findCategoryById(category_id);
        categoryRepository.deleteById(category_id);
        return null;
    }
}
