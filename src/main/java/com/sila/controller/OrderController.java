package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.CategoryResponse;
import com.sila.dto.response.OrderResponse;
import com.sila.service.OrderService;
import com.sila.util.annotation.PreAuthorization;
import com.sila.util.enums.ROLE;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order")
@RequiredArgsConstructor
public class OrderController {
    final OrderService orderService;
    @GetMapping
    public ResponseEntity<EntityResponseHandler<OrderResponse>> getOrder(@ModelAttribute PaginationRequest request) {
        return new ResponseEntity<>(orderService.getAll(request), HttpStatus.OK);
    }
    @PreAuthorization({ROLE.USER})
    @PostMapping()
    public ResponseEntity<OrderResponse> placeOrder() {
        return ResponseEntity.ok(orderService.placeOrder());
    }
    @DeleteMapping("{orderId}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.deletePlaceOrder(orderId));
    }

}
