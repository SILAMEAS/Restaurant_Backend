package com.sila.service;

import com.sila.dto.response.OrderItemResponse;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponse> getAll();
}
