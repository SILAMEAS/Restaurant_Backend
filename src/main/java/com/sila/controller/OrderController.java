package com.sila.controller;

import com.sila.dto.response.OrderResponse;
import com.sila.model.Order;
import com.sila.model.OrderItem;
import com.sila.service.CategoryService;
import com.sila.service.OrderItemService;
import com.sila.service.OrderService;
import com.sila.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


}
