package com.sila.controller.admin;

import com.sila.dto.request.CategoryRequest;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Category Controller", description = "Admin operations related to category")
@RestController
@RequestMapping("api/admin/categories/")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
    @PutMapping("{categoryId}")
    public ResponseEntity<Category> editCategory(@RequestHeader("Authorization") String jwt, @RequestBody CategoryRequest categoryReq, @PathVariable Long categoryId) throws Exception {
        return new ResponseEntity<>(categoryService.editCategory(jwt,categoryReq.getName(),categoryId), HttpStatus.CREATED);
    }
    @DeleteMapping("{category_id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long category_id) throws Exception {
        categoryService.deleteCategoryById(category_id);
        return new ResponseEntity<>("Category id : "+category_id+" deleted", HttpStatus.CREATED);
    }
}
