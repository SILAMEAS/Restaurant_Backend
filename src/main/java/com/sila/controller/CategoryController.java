package com.sila.controller;

import com.sila.dto.request.CategoryRequest;
import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Category Controller", description = "User operations related to Category")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorization({ROLE.ADMIN, ROLE.OWNER})
    public ResponseEntity<Category> createCategory(@RequestHeader("Authorization") String jwt, @RequestBody CategoryRequest categoryReq) {
        return new ResponseEntity<>(categoryService.create(jwt, categoryReq.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getRestaurantCategory(@PathVariable Long restaurantId) {
        List<Category> categoriesInRestaurant = categoryService.getsByResId(restaurantId);
        return new ResponseEntity<>(categoriesInRestaurant, HttpStatus.OK);
    }
    @PreAuthorization({ROLE.ADMIN, ROLE.OWNER})
    @PutMapping("{categoryId}")
    public ResponseEntity<Category> editCategory(@RequestBody CategoryRequest categoryReq, @PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.update(categoryReq.getName(), categoryId), HttpStatus.CREATED);
    }
    @PreAuthorization({ROLE.ADMIN, ROLE.OWNER})
    @DeleteMapping("{categoryId}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.delete(categoryId), HttpStatus.CREATED);
    }
}
