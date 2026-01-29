package com.sila.modules.order.repository;

import com.sila.modules.food.model.Food;
import com.sila.modules.order.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteAllByFood(Food food);

    List<OrderItem> findAllByFood(Food food);
}
