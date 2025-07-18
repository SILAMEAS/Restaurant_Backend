package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.method.OnCreate;
import com.sila.dto.method.OnUpdate;
import com.sila.dto.request.CategoryRequest;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import com.sila.service.FoodService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Category Controller")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<EntityResponseHandler<CategoryResponse>> getCategories(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(categoryService.gets(request));
    }

    @PostMapping
    @PreAuthorization({ROLE.ADMIN, ROLE.OWNER})
    public ResponseEntity<Category> createCategory(@Validated(OnCreate.class) @ModelAttribute  CategoryRequest request) {
        return new ResponseEntity<>(categoryService.create(request), HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<CategoryResponse>> getRestaurantCategory(@PathVariable Long restaurantId) {
        List<CategoryResponse> categoriesInRestaurant = categoryService.getsByResId(restaurantId).stream().map(f->modelMapper.map(f, CategoryResponse.class)).toList();
        return new ResponseEntity<>(categoriesInRestaurant, HttpStatus.OK);
    }
    @PreAuthorization({ROLE.ADMIN, ROLE.OWNER})
    @PutMapping("{categoryId}")
    public ResponseEntity<Category> editCategory(@Validated(OnUpdate.class) @ModelAttribute  CategoryRequest request, @PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.update(request, categoryId), HttpStatus.CREATED);
    }
    @PreAuthorization({ROLE.ADMIN, ROLE.OWNER})
    @DeleteMapping("{categoryId}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.delete(categoryId), HttpStatus.CREATED);
    }

    @PreAuthorization({ROLE.ADMIN})
    @DeleteMapping("/bulk")
    public ResponseEntity<String> deleteAll() {
        return new ResponseEntity<>(categoryService.deleteAllCategories(), HttpStatus.CREATED);
    }


}
