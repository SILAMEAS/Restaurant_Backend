package com.sila.controller;

import com.sila.dto.response.CartResponse;
import com.sila.service.CartService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cart Controller", description = "User operations related to Cart")
@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    final CartService cartService;
    @PreAuthorization({ROLE.USER})
    @PostMapping()
    public ResponseEntity<String> addToCart(@RequestParam Long foodId, @RequestParam int quantity) throws Exception {
        cartService.addItemToCart( foodId, quantity);
        return ResponseEntity.ok("Item added to cart");
    }


    @GetMapping()
    public ResponseEntity<CartResponse> addToCart() throws Exception {
        return new ResponseEntity<>(cartService.getAll(), HttpStatus.OK);
    }

    @PreAuthorization({ROLE.USER})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long id) throws Exception {

        cartService.removeItemFromCart(id);
        return ResponseEntity.ok("Item remove from cart");
    }
}
