package com.sila.controller.admin;

import com.sila.dto.request.CategoryRequest;
import com.sila.dto.response.MessageResponse;
import com.sila.model.Category;
import com.sila.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Category Controller", description = "Admin operations related to category")
@RestController
@RequestMapping("api/admin/categories/")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PutMapping("{categoryId}")
    public ResponseEntity<Category> editCategory(@RequestBody CategoryRequest categoryReq, @PathVariable Long categoryId) {
        return new ResponseEntity<>(categoryService.editCategory(categoryReq.getName(), categoryId), HttpStatus.CREATED);
    }

    @DeleteMapping("{categoryId}")
    public ResponseEntity<MessageResponse> deleteCategory(@PathVariable Long categoryId) throws Exception {
        return new ResponseEntity<>(categoryService.deleteCategoryById(categoryId), HttpStatus.CREATED);
    }
}
