package com.sila.service.lmp;

import com.sila.dto.request.OrderItemRequest;
import com.sila.dto.response.OrderItemResponse;
import com.sila.model.Order;
import com.sila.model.OrderItem;
import com.sila.service.OrderItemService;
import com.sila.service.OrderService;
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
