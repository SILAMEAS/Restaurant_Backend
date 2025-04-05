package com.sila.service;

import com.sila.model.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(String jwt,String name) throws Exception;
    public List<Category> listCategoriesByRestaurantId(Long restaurant_id);

    Category editCategory(String jwt, String name, Long categoryId) throws Exception;

    public Category findCategoryById(Long category_id)throws Exception;
    public void deleteCategoryById(Long category_id)throws Exception;
}
