package com.sila.controller.api;

import com.sila.dto.request.CategoryReq;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/admin/categories")
    public ResponseEntity<Category> createCategory(@RequestHeader("Authorization") String jwt, @RequestBody CategoryReq categoryReq) throws Exception {
        return new ResponseEntity<>(categoryService.createCategory(jwt,categoryReq.getName()), HttpStatus.CREATED);
    }
    @GetMapping("/categories/restaurant/{restaurantId}")
    public ResponseEntity<List<Category>> getRestaurantCategory(@PathVariable Long restaurantId){
        List<Category> categoriesInRestaurant=categoryService.listCategoriesByRestaurantId(restaurantId);
        return new ResponseEntity<>(categoriesInRestaurant, HttpStatus.OK);
    }
}
