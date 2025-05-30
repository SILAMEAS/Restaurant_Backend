package com.sila.repository;

import com.sila.model.Food;
import com.sila.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long>, PagingAndSortingRepository<Food, Long>, JpaSpecificationExecutor<Food> {

    List<Food> findAllByCategoryId(Long categoryId);

    void deleteAllByCategoryId(Long categoryId);

    Long countAllByRestaurant(Restaurant restaurant);

    @Query("SELECT f FROM Food f WHERE " +
            "(:search IS NULL OR LOWER(f.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
            "(:filter IS NULL OR f.category = :filter)")
    Page<Food> searchFood(
            @Param("search") String search,
            @Param("filter") String filter,
            Pageable pageable
    );
}
