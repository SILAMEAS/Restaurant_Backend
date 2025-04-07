package com.sila.controller.admin;

import com.sila.dto.request.CategoryRequest;
import com.sila.exception.NotFoundException;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Admin Category Controller", description = "Admin operations related to category")
@RestController
@RequestMapping("api/admin/categories/")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
    @PutMapping("{categoryId}")
    public ResponseEntity<Category> editCategory(@RequestBody CategoryRequest categoryReq, @PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.editCategory(categoryReq.getName(),categoryId), HttpStatus.CREATED);
    }
    @DeleteMapping("{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) throws Exception {
        Optional<Category> category=categoryService.deleteCategoryById(categoryId);
        if(category.isPresent()){
            return new ResponseEntity<>("Category id : "+categoryId+" deleted", HttpStatus.CREATED);
        }
        throw new NotFoundException("Not Found Category with this id "+categoryId);
    }
}
