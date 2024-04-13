package com.sila.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sila.utlis.enums.ORDER_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.Stack;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  private User customer;
  @JsonIgnore
  @ManyToOne
  private Restaurant restaurant;
  private Long totalAmount;
  private String orderStatus;
  private Date createdAt;
  @ManyToOne
  private Address deliveryAddress;
  @OneToMany
  private List<OrderItem> orderItems;
//  private Payment payment;
  private  int totalItem;
  private double totalPrice;

}
