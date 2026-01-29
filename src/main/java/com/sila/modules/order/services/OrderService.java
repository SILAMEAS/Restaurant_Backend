package com.sila.modules.order.services;

import com.sila.share.pagination.EntityResponseHandler;
import com.sila.share.dto.req.PaginationRequest;
import com.sila.modules.order.dto.OrderResponse;

public interface OrderService {

    EntityResponseHandler<OrderResponse> getAll(PaginationRequest request);

    OrderResponse placeOrder(Long cartId);

    String deletePlaceOrder(Long orderId);

    String deleteAllPlaceOrderInRestaurant();
}
