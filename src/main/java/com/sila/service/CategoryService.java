package com.sila.service;

import com.sila.dto.request.CategoryReq;
import com.sila.model.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(String jwt,String name) throws Exception;
    public List<Category> listCategoriesByRestaurantId(Long restaurant_id);
    public Category findCategoryById(Long category_id)throws Exception;
    public Void deleteCategoryById(Long category_id)throws Exception;
}
