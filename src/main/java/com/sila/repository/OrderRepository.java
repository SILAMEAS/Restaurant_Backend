package com.sila.repository;

import com.sila.model.Order;
import com.sila.model.Restaurant;
import com.sila.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o.user FROM Order o WHERE o.restaurant.id = :restaurantId")
    List<User> findUsersByRestaurantId(@Param("restaurantId") Long restaurantId);

    Page<Order> findAllByUser(User user, Pageable pageable);

    void deleteAllByRestaurant(Restaurant restaurant);

    List<Order> findAllByRestaurant(Restaurant restaurant);

    Page<Order> findAllByRestaurant(Restaurant restaurant,Pageable pageable);
}
