package com.sila.controller;

import com.sila.dto.response.OrderResponse;
import com.sila.service.OrderService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order")
@RequiredArgsConstructor
public class OrderController {
    final OrderService orderService;
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrder() {
        return new ResponseEntity<>(orderService.getAll(), HttpStatus.OK);
    }
    @PreAuthorization({ROLE.USER})
    @PostMapping()
    public ResponseEntity<OrderResponse> placeOrder() {
        return ResponseEntity.ok(orderService.placeOrder());
    }

}
