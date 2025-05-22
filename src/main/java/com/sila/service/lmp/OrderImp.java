package com.sila.service.lmp;

import com.sila.dto.response.OrderResponse;
import com.sila.model.Order;
import com.sila.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderImp implements OrderService {
    @Override
    public List<OrderResponse> getAll() {
        return List.of();
    }

    @Override
    public OrderResponse placeOrder(Long userId) {
        return null;
    }
}
