package com.sila.service;

import com.sila.dto.request.CartRequest;
import com.sila.dto.response.CartResponse;
import com.sila.model.Cart;
import com.sila.model.Order;

import java.util.List;

public interface CartService {

    List<CartResponse> getAll();
}
