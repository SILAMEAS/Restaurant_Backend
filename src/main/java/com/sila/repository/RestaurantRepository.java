package com.sila.repository;

import com.sila.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long>,
        JpaSpecificationExecutor<Restaurant> {

    @Query("SELECT f FROM Restaurant f WHERE f.name LIKE %:keyword% OR f.cuisineType LIKE %:keyword%")
    List<Restaurant> findBySearchQuery(@Param("keyword") String query);

    Optional<Restaurant> findByOwnerId(Long ownerId);

    boolean existsByOwnerId(Long ownerId);

    List<Restaurant> findAllByAddressId(Long addressId);


}
