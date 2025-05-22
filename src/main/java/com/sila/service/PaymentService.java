package com.sila.service;

import com.sila.dto.response.PaymentResponse;
import com.sila.util.enums.PAYMENT_METHOD;

public interface PaymentService {

    PaymentResponse payForOrder(Long orderId, PAYMENT_METHOD method);
}
