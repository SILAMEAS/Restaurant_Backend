package com.sila.modules.payment.services;

import com.sila.modules.payment.dto.PaymentResponse;
import com.sila.modules.payment.PAYMENT_METHOD;

public interface PaymentService {

    PaymentResponse payForOrder(Long orderId, PAYMENT_METHOD method);
}
