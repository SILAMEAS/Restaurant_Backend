package com.sila.repository;

import com.sila.model.Favorite;
import com.sila.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Modifying
    @Transactional
    void deleteByRestaurantId(Long restaurantId);

    boolean existsFavoriteByRestaurantId(Long restaurantId);

    List<Favorite> findAllByOwner(User owner);
}
