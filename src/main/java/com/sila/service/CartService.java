package com.sila.service;

import com.sila.dto.response.CartResponse;

public interface CartService {

    CartResponse getAll() throws Exception;

    void addItemToCart( Long foodId, int quantity) throws Exception;

    void removeItemFromCart(Long cartItemId) throws Exception;

    void updateItemFromCart(Long cartItemId,int quantity) throws Exception;
}
