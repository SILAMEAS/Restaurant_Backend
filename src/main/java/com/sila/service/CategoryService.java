package com.sila.service;

import com.sila.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(String jwt,String name) throws Exception;
    List<Category> listCategoriesByRestaurantId(Long restaurant_id);

    Category editCategory(String jwt, String name, Long categoryId) throws Exception;

    Category findCategoryById(Long category_id)throws Exception;
    void deleteCategoryById(Long category_id)throws Exception;
}
