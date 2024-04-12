package com.sila.repository;

import com.sila.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    public List<Order> findByCustomerId(Long user_id);
    public List<Order> findByRestaurantId(Long restaurant_id);
}
