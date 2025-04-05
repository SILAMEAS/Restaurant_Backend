package com.sila.repository;

import com.sila.model.Category;
import com.sila.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public List<Category> findByRestaurantId(Long restaurant_id);
    boolean existsByNameAndRestaurant(String name, Restaurant restaurant);
}
