package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.CategoryRequest;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(CategoryRequest request);

    List<Category> getsByResId(Long restaurantId);

    Category update(CategoryRequest request, Long categoryId);

    Category getById(Long categoryId);

    MessageResponse delete(Long categoryId);

    List<CategoryResponse> all();
    EntityResponseHandler<CategoryResponse> gets(PaginationRequest request);

    String deleteAllCategories();


}
