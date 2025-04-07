package com.sila.controller.api;

import com.sila.dto.request.CategoryRequest;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User Category Controller", description = "User operations related to Category")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    public ResponseEntity<Category> createCategory(@RequestHeader("Authorization") String jwt, @RequestBody CategoryRequest categoryReq) throws Exception {
        return new ResponseEntity<>(categoryService.createCategory(jwt, categoryReq.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/categories/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getRestaurantCategory(@PathVariable Long restaurantId) {
        List<Category> categoriesInRestaurant = categoryService.listCategoriesByRestaurantId(restaurantId);
        return new ResponseEntity<>(categoriesInRestaurant, HttpStatus.OK);
    }
}
