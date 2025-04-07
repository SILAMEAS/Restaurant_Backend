package com.sila.specifcation;

import com.sila.model.Category_;
import com.sila.model.Food;
import com.sila.model.Food_;
import com.sila.model.Restaurant_;
import org.springframework.data.jpa.domain.Specification;

public class FoodSpecification {
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
}