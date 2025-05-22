package com.sila.service;

import com.sila.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    List<CartResponse> getAll() throws Exception;

    void addItemToCart( Long foodId, int quantity) throws Exception;
}
