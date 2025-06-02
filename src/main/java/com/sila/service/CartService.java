package com.sila.service;

import com.sila.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    List<CartResponse> getAll() throws Exception;

    void addItemToCart( Long foodId, int quantity) throws Exception;

    void removeItemFromCart(Long cartItemId) throws Exception;

    void updateItemFromCart(Long cartItemId,int quantity) throws Exception;

    void deleteCart(Long cartId);
}
