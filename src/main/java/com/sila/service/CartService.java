package com.sila.service;

import com.sila.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    List<CartResponse> getAll() throws Exception;

    void addItemToCart( Long foodId, int quantity) throws Exception;

    void removeItemFromCart(Long cartId,Long cartItemId) throws Exception;

    void updateItemFromCart(Long CartId,Long cartItemId,int quantity) throws Exception;

    void deleteCart(Long cartId);
}
