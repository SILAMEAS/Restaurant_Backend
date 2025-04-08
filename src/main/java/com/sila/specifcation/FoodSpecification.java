package com.sila.specifcation;

import com.sila.dto.request.SearchRequest;
import com.sila.model.Category_;
import com.sila.model.Food;
import com.sila.model.Food_;
import com.sila.model.Restaurant_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public final class FoodSpecification {
    public static Specification<Food> search(String search) {
        if (search == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Food_.NAME), search + "%");

    }

    public static Specification<Food> filterCategory(String category) {
        if (category == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Food_.category).get(Category_.NAME), category);

    }

    public static Specification<Food> bySession(Boolean sessional) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Food_.IS_SEASONAL), sessional);

    }

    public static Specification<Food> byVegetarian(Boolean vegetarian) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Food_.IS_VEGETARIAN), vegetarian);

    }

    public static Specification<Food> filterByRestaurantId(Long resId) {
        if (resId == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Food_.RESTAURANT).get(Restaurant_.ID), resId);

    }
    /**
     * ==============================  Specification  ========================================================
     **/
    public static Specification<Food> filterFood(SearchRequest searchReq, String filterBy) {
        Specification<Food> spec = Specification.where(null);
        if (Objects.nonNull(searchReq.getSearch())) {
            spec = spec.and(FoodSpecification.search(searchReq.getSearch()));
        }
        if (Objects.nonNull(filterBy)) {
            spec = spec.and(FoodSpecification.filterCategory(filterBy));
        }
        if (Boolean.TRUE.equals(searchReq.getSessional())) {
            spec = spec.and(FoodSpecification.bySession(true));
        }
        if (Boolean.TRUE.equals(searchReq.getVegeterain())) {
            spec = spec.and(FoodSpecification.byVegetarian(true));
        }
        return spec;
    }

    public static Specification<Food> filterFoodByRestaurantId(Long restaurantId, SearchRequest searchReq, String filterBy) {
        Specification<Food> spec = Specification.where(null);
        if (Objects.nonNull(filterBy)) {
            spec = spec.and(FoodSpecification.filterCategory(filterBy));
        }
        if (Boolean.TRUE.equals(searchReq.getSessional())) {
            spec = spec.and(FoodSpecification.bySession(true));
        }
        if (Boolean.TRUE.equals(searchReq.getVegeterain())) {
            spec = spec.and(FoodSpecification.byVegetarian(true));
        }
        if (Objects.nonNull(restaurantId)) {
            spec = spec.and(FoodSpecification.filterByRestaurantId(restaurantId));
        }
        return spec;
    }
}