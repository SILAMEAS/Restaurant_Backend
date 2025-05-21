package com.sila.repository;

import com.sila.model.Order;
import com.sila.model.OrderItem;
import com.sila.model.OrderItem_;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
