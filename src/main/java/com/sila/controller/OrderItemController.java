package com.sila.controller;

import com.sila.dto.response.OrderItemResponse;
import com.sila.model.Order;
import com.sila.model.OrderItem;
import com.sila.service.OrderItemService;
import com.sila.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orderItem")
@Tag(name = "OrderItem")
@RequiredArgsConstructor
public class OrderItemController {
    final OrderItemService orderItemService;
    @GetMapping()
    public ResponseEntity<List<OrderItemResponse>> getOrderItem() {
        return new ResponseEntity<>(orderItemService.getAll(), HttpStatus.OK);
    }

}
