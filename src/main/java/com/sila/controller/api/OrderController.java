package com.sila.controller.api;

import com.sila.dto.request.OrderReq;
import com.sila.model.Order;
import com.sila.model.User;
import com.sila.service.OrderService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    @PostMapping()
    public ResponseEntity<Order> createOrder(@RequestHeader("Authorization") String jwt, @RequestBody OrderReq orderReq) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Order order=orderService.createOrder(orderReq,user);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
    @GetMapping("/user")
    public ResponseEntity<List<Order>> listHistoryOrderByUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> listOrderByUserId = orderService.getUserOrder(user.getId());
        return new ResponseEntity<>(listOrderByUserId, HttpStatus.OK);
    }
}
