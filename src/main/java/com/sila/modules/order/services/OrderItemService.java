package com.sila.modules.order.services;

import com.sila.modules.order.dto.OrderItemResponse;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponse> getAll();
}
