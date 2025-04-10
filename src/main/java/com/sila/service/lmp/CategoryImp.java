package com.sila.service.lmp;

import com.sila.dto.response.MessageResponse;
import com.sila.exception.AccessDeniedException;
import com.sila.exception.BadRequestException;
import com.sila.model.Category;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.repository.CategoryRepository;
import com.sila.repository.FoodRepository;
import com.sila.repository.RestaurantRepository;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import com.sila.config.context.UserContext;
import com.sila.util.enums.USER_ROLE;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryImp implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    private final FoodService foodService;
    private final FoodRepository foodRepository;

    @Override
    public Category create(String jwt, String name) {
        User user = UserContext.getUser();
        Restaurant restaurant = restaurantRepository.findByOwnerId(user.getId());
        if (categoryRepository.existsByNameAndRestaurant(name, restaurant)) {
            throw new BadRequestException("Category name already exists for this restaurant");
        }
        Category category = Category.builder().name(name).restaurant(restaurant).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category update(String name, Long categoryId) {
        USER_ROLE user = UserContext.getUser().getRole();
        if (user.equals(USER_ROLE.ROLE_CUSTOMER)) {
            throw new AccessDeniedException("Customer do not have permission to change category");
        }
        Category category = getById(categoryId);
        if (!name.isEmpty()) {
            category.setName(name);
        }
        return categoryRepository.save(category);
    }


    @Override
    public List<Category> getsByResId(Long restaurantId) {
        return categoryRepository.findByRestaurantId(restaurantId);
    }

    @Override
    public MessageResponse delete(Long categoryId) {
        getById(categoryId);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new BadRequestException("category not found"));
        var foods = foodRepository.findAllByCategoryId(category.getId());
        if (!foods.isEmpty()) {
            foodService.deleteByCategoryId(categoryId);
        }
        categoryRepository.deleteById(categoryId);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Category Id : " + categoryId + " successfully!");
        return messageResponse;
    }

    public Category getById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new BadRequestException("category not found"));
    }

}
