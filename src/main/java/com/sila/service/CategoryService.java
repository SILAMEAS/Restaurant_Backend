package com.sila.service;

import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(String jwt, String name);

    List<Category> getsByResId(Long restaurantId);

    Category update(String name, Long categoryId);

    Category getById(Long categoryId);

    MessageResponse delete(Long categoryId);

}
