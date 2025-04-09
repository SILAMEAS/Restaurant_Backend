package com.sila.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders") // "order" is a reserved keyword in many databases
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to user who placed the order
    @ManyToOne
    private User user;

    // List of items (snapshot of what was in the cart at the time)
    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> items;

    private double totalAmount;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

    private String status; // e.g., "PENDING", "CONFIRMED", "DELIVERED", "CANCELLED"
}
