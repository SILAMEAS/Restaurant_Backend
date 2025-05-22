package com.sila.controller;

import com.sila.dto.response.CartResponse;
import com.sila.dto.response.OrderResponse;
import com.sila.dto.response.PaymentResponse;
import com.sila.model.Order;
import com.sila.model.OrderItem;
import com.sila.service.CartService;
import com.sila.service.CategoryService;
import com.sila.service.OrderItemService;
import com.sila.service.OrderService;
import com.sila.service.PaymentService;
import com.sila.service.UserService;
import com.sila.util.enums.PAYMENT_METHOD;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@Tag(name = "Order")
@RequiredArgsConstructor
public class OrderController {
    final OrderService orderService;
    final CartService cartService;
    final PaymentService paymentService;
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrder() {
        return new ResponseEntity<>(orderService.getAll(), HttpStatus.OK);
    }



    @PostMapping("/carts")
    public ResponseEntity<String> addToCart( @RequestParam Long foodId, @RequestParam int quantity) throws Exception {
        cartService.addItemToCart( foodId, quantity);
        return ResponseEntity.ok("Item added to cart");
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartResponse>> addToCart() throws Exception {
        return new ResponseEntity<>(cartService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/place-order")
    public ResponseEntity<OrderResponse> placeOrder(@RequestParam Long userId) {
        return ResponseEntity.ok(orderService.placeOrder(userId));
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentResponse> pay(@RequestParam Long orderId, @RequestParam PAYMENT_METHOD method) {
        return ResponseEntity.ok(paymentService.payForOrder(orderId, method));
    }


}
