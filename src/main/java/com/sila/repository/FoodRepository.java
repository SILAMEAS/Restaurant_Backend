package com.sila.repository;

import com.sila.model.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FoodRepository extends JpaRepository<Food,Long>, PagingAndSortingRepository<Food,Long> , JpaSpecificationExecutor<Food> {
    Page<Food> findAllByRestaurantId(Long restaurant_id,Specification<Food> specification, Pageable pageable);
}
