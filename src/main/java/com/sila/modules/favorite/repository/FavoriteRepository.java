package com.sila.modules.favorite.repository;

import com.sila.modules.favorite.model.Favorite;
import com.sila.modules.profile.model.User;
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
