package com.sila.service.lmp;

import com.sila.dto.response.OrderItemResponse;
import com.sila.service.OrderItemService;
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
