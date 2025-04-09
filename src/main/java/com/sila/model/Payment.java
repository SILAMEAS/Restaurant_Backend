package com.sila.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Order order;

    private String paymentMethod; // e.g., "CASH", "CARD", "ONLINE"

    private double amount;

    private LocalDateTime paidAt;

    private String status; // e.g., "PENDING", "PAID", "FAILED"
}
