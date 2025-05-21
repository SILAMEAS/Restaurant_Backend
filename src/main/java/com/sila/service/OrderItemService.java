package com.sila.service;

import com.sila.dto.request.OrderItemRequest;
import com.sila.dto.response.OrderItemResponse;
import com.sila.model.Order;
import com.sila.model.OrderItem;

import java.util.List;

public interface OrderItemService {

    List<OrderItemResponse> getAll();
}
