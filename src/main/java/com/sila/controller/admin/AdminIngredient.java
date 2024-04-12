package com.sila.controller.admin;

import com.sila.model.IngredientCategory;
import com.sila.model.IngredientsItem;
import com.sila.dto.request.IngredientReq;
import com.sila.dto.request.IngredientItemReq;
import com.sila.service.IngredientCategoryService;
import com.sila.service.IngredientItemService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/admin/ingredient")
@RequiredArgsConstructor
public class AdminIngredient {
    private final IngredientItemService ingredientItemService;
    private final IngredientCategoryService ingredientCategoryService;
    private final UserService userService;
    @PostMapping("/category")
    public ResponseEntity<IngredientCategory> createIngredientCategory(@RequestHeader("Authorization") String jwt, @RequestBody IngredientReq req) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(ingredientCategoryService.createIngredientCategory(req.getName(),req.getRestaurantId()), HttpStatus.CREATED);
    }
    @PostMapping("/items")
    public ResponseEntity<IngredientsItem> createIngredientCategory(@RequestHeader("Authorization") String jwt, @RequestBody IngredientItemReq req) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(ingredientItemService.createIngredientItem(req.getRestaurantId(), req.getName(), req.getCategoryId()), HttpStatus.OK);
    }
    @PutMapping("/items/{ingredientItemId}/stock")
    public ResponseEntity<IngredientsItem> updateStockIngredient(@RequestHeader("Authorization") String jwt,@PathVariable Long ingredientItemId) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(ingredientItemService.updateIngredientItemStock(ingredientItemId), HttpStatus.OK);
    }
    @GetMapping("/restaurant/{restaurantId}/ingredient-category")
    public ResponseEntity<List<IngredientCategory>> getIngredientCategoryByRestaurantId(@RequestHeader("Authorization") String jwt, @PathVariable Long restaurantId) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(ingredientCategoryService.findIngredientCategoryByRestaurantId(restaurantId), HttpStatus.CREATED);
    }
    @GetMapping("/restaurant/{restaurantId}/ingredient-item")
    public ResponseEntity<List<IngredientsItem>> getIngredientItemByRestaurantId(@RequestHeader("Authorization") String jwt, @PathVariable Long restaurantId) throws Exception {
        userService.findUserByJwtToken(jwt);
        return new ResponseEntity<>(ingredientItemService.findIngredientItemsByRestaurantId(restaurantId), HttpStatus.CREATED);
    }
}

