package com.sila.modules.order.services;

import com.sila.modules.order.dto.OrderItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemImp implements OrderItemService {
    @Override
    public List<OrderItemResponse> getAll() {
        return List.of();
    }
}
