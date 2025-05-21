package com.sila.service.lmp;

import com.sila.dto.response.CartResponse;
import com.sila.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartImp implements CartService {
    @Override
    public List<CartResponse> getAll() {
        return List.of();
    }
}
