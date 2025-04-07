package com.sila.service;

import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category createCategory(String jwt,String name);
    List<Category> listCategoriesByRestaurantId(Long restaurantId);
    Category editCategory( String name, Long categoryId);
    Category findCategoryById(Long categoryId);
    MessageResponse deleteCategoryById(Long categoryId) throws Exception;
}
