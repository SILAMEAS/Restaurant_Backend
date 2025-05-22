package com.sila.service.lmp;

import com.sila.dto.response.PaymentResponse;
import com.sila.service.PaymentService;
import com.sila.util.enums.PAYMENT_METHOD;
import org.springframework.stereotype.Service;


@Service
public class PaymentImp implements PaymentService {
    @Override
    public PaymentResponse payForOrder(Long orderId, PAYMENT_METHOD method) {
        return null;
    }
}
