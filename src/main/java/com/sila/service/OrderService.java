package com.sila.service;

import com.sila.dto.response.OrderResponse;
import com.sila.model.Order;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAll();

    OrderResponse placeOrder();
}
