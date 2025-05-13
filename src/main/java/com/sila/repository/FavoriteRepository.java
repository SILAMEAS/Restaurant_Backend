package com.sila.repository;

import com.sila.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    void deleteByRestaurantId(Long restaurant_id);
}
