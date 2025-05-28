package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    EntityResponseHandler<OrderResponse> getAll(PaginationRequest request);

    OrderResponse placeOrder();

    String deletePlaceOrder(Long orderId);
}
