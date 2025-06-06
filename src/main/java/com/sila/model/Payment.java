package com.sila.model;


import com.sila.util.enums.PAYMENT_METHOD;
import com.sila.util.enums.PAYMENT_STATUS;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    private PAYMENT_METHOD paymentMethod;

    private double amount;

    private LocalDateTime paidAt;

    private PAYMENT_STATUS status;
}
