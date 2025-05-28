package com.sila.repository;

import com.sila.model.Food;
import com.sila.model.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long>, PagingAndSortingRepository<Food, Long>, JpaSpecificationExecutor<Food> {
    Page<Food> findAllByRestaurantId(Long restaurant_id, Specification<Food> specification, Pageable pageable);

    List<Food> findAllByCategoryId(Long categoryId);

    // Search by food name (LIKE query)
    List<Food> findByNameStartingWithIgnoreCase(String search);

    // Filter by category name
    List<Food> findByCategoryName(String category);

    // Filter by seasonal status
    List<Food> findByIsSeasonal(Boolean isSeasonal);

    // Filter by vegetarian status
    List<Food> findByIsVegetarian(Boolean isVegetarian);

    // Filter by restaurant ID
    List<Food> findByRestaurantId(Long restaurantId);

    // Combined query for filterFood(SearchRequest searchReq, String filterBy)
    @Query("SELECT f FROM Food f " +
            "WHERE (:search IS NULL OR f.name LIKE :search%) " +
            "AND (:category IS NULL OR f.category.name = :category) " +
            "AND (:isSeasonal IS NULL OR f.isSeasonal = :isSeasonal) " +
            "AND (:isVegetarian IS NULL OR f.isVegetarian = :isVegetarian)")
    List<Food> findFoodsByCriteria(@Param("search") String search,
                                   @Param("category") String category,
                                   @Param("isSeasonal") Boolean isSeasonal,
                                   @Param("isVegetarian") Boolean isVegetarian);

    // Combined query for filterFoodByRestaurantId(Long restaurantId, SearchRequest searchReq, String filterBy)
    @Query("SELECT f FROM Food f " +
            "WHERE (:restaurantId IS NULL OR f.restaurant.id = :restaurantId) " +
            "AND (:category IS NULL OR f.category.name = :category) " +
            "AND (:isSeasonal IS NULL OR f.isSeasonal = :isSeasonal) " +
            "AND (:isVegetarian IS NULL OR f.isVegetarian = :isVegetarian)")
    List<Food> findFoodsByRestaurantAndCriteria(@Param("restaurantId") Long restaurantId,
                                                @Param("category") String category,
                                                @Param("isSeasonal") Boolean isSeasonal,
                                                @Param("isVegetarian") Boolean isVegetarian);

    void deleteAllByCategoryId(Long categoryId);

    Long countAllByRestaurant(Restaurant restaurant);
}
