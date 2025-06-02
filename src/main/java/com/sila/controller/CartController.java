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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @DeleteMapping("{cartId}")
    public ResponseEntity<String> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart( cartId);
        return ResponseEntity.ok("remove cart already");
    }


    @PreAuthorization({ROLE.USER})
    @GetMapping()
    public ResponseEntity<List<CartResponse>> getMyCart() throws Exception {
        return new ResponseEntity<>(cartService.getAll(),HttpStatus.OK);
    }
    @PreAuthorization({ROLE.USER})
    @DeleteMapping("{cartId}/cartItems/{cartItemId}")
    public ResponseEntity<String> deleteCartItem(
            @PathVariable Long cartId,
            @PathVariable Long cartItemId) throws Exception {

        cartService.removeItemFromCart(cartId,cartItemId);
        return ResponseEntity.ok("Item remove from cart");
    }

    @PreAuthorization({ROLE.USER})
    @PutMapping("{cartId}/cartItems/{cartItemId}")
    public ResponseEntity<String> updateCartItem(
            @PathVariable Long cartId,@PathVariable Long cartItemId, @RequestParam int quantity ) throws Exception {

        cartService.updateItemFromCart(cartId,cartItemId,quantity);
        return ResponseEntity.ok("Item was update in cart");
    }
}
