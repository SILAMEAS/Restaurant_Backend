package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.OrderResponse;

public interface OrderService {

    EntityResponseHandler<OrderResponse> getAll(PaginationRequest request);

    OrderResponse placeOrder(Long cartId);

    String deletePlaceOrder(Long orderId);

    String deleteAllPlaceOrderInRestaurant();
}
