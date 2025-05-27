package com.sila.service;

import com.sila.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    List<OrderResponse> getAll();

    OrderResponse placeOrder();
}
