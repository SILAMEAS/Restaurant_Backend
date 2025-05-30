package com.sila.repository;

import com.sila.model.Food;
import com.sila.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteAllByFood(Food food);

    List<OrderItem> findAllByFood(Food food);
}
