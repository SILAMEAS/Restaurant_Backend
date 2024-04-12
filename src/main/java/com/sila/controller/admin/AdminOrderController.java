package com.sila.controller.admin;

import com.sila.model.Order;
import com.sila.model.Restaurant;
import com.sila.model.User;
import com.sila.service.CardService;
import com.sila.service.OrderService;
import com.sila.service.RestaurantService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final CardService cardService;
    private final RestaurantService restaurantService;
    @GetMapping("/restaurant")
    public ResponseEntity<List<Order>> listOrderByUser(@RequestHeader("Authorization") String jwt, @RequestParam(required = false) String orderStatus) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Restaurant restaurant=restaurantService.getRestaurantByUserId(user.getId());
        List<Order> OwnerListOrderInRestaurant= orderService.getRestaurantOrder(restaurant.getId(),orderStatus);
        return new ResponseEntity<>(OwnerListOrderInRestaurant, HttpStatus.OK);
    }
    @GetMapping("/restaurant/{orderId}")
    public ResponseEntity<Order> listOrderByUser(@RequestHeader("Authorization") String jwt,@PathVariable Long orderId) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order orderDetail=orderService.getOrderById(orderId);
        return new ResponseEntity<>(orderDetail, HttpStatus.OK);
    }
    @PutMapping("/restaurant/{orderId}/update-status-order/{updateStatusOrder}")
    public ResponseEntity<Order> UpdateStatusOrderInListOrder(@RequestHeader("Authorization") String jwt, @PathVariable Long orderId,@PathVariable String updateStatusOrder) throws Exception {
        userService.findUserByJwtToken(jwt);
        Order orderUpdated=orderService.updateOrder(orderId,updateStatusOrder);
        return new ResponseEntity<>(orderUpdated, HttpStatus.OK);
    }
}
