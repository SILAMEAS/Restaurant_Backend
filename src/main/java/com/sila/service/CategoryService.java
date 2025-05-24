package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.request.SearchRequest;
import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.FoodResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Category create(String jwt, String name);

    List<Category> getsByResId(Long restaurantId);

    Category update(String name, Long categoryId);

    Category getById(Long categoryId);

    MessageResponse delete(Long categoryId);

    List<CategoryResponse> all();
    EntityResponseHandler<CategoryResponse> gets(PaginationRequest request);


}
