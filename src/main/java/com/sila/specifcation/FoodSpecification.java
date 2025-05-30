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
        spec = addSearchSpecification(spec, searchReq);
        spec = addFilterSpecification(spec, filterBy);

        if(Objects.equals(filterBy, "sessional")){
            spec = addSessionalSpecification(spec, true);
        }
        if(Objects.equals(filterBy, "vegeterain")){
            spec = addVegetarianSpecification(spec, true);
        }
//        spec = addSessionalSpecification(spec, searchReq);
//        spec = addVegetarianSpecification(spec, searchReq);
        return spec;
    }

    public static Specification<Food> filterFoodByRestaurantId(Long restaurantId, SearchRequest searchReq, String filterBy) {
        Specification<Food> spec = Specification.where(null);
        spec= addFilterSpecification(spec,filterBy);
        spec = addSessionalSpecification(spec, Objects.equals(filterBy, "sessional"));
        spec = addVegetarianSpecification(spec,  Objects.equals(filterBy, "vegeterain"));
        spec = addRestaurantIdSpecification(spec,restaurantId);
        return spec;
    }
    /**
     * ==============================  Re-usable method  ========================================================
     **/
    private static Specification<Food> addRestaurantIdSpecification(Specification<Food> spec, Long restaurantId) {
        if (Objects.nonNull(restaurantId)) {
            return spec.and(FoodSpecification.filterByRestaurantId(restaurantId));
        }
        return spec;
    }
    private static Specification<Food> addSearchSpecification(Specification<Food> spec, SearchRequest searchReq) {
        if (Objects.nonNull(searchReq.getSearch())) {
            return spec.and(FoodSpecification.search(searchReq.getSearch()));
        }
        return spec;
    }

    private static Specification<Food> addFilterSpecification(Specification<Food> spec, String filterBy) {
        if (Objects.nonNull(filterBy)) {
            return spec.and(FoodSpecification.filterCategory(filterBy));
        }
        return spec;
    }

    private static Specification<Food> addSessionalSpecification(Specification<Food> spec,Boolean sessional) {
        if (Boolean.TRUE.equals(sessional)) {
            return spec.and(FoodSpecification.bySession(true));
        }
        return spec;
    }

    private static Specification<Food> addVegetarianSpecification(Specification<Food> spec,Boolean vegeterain) {
        if (Boolean.TRUE.equals(vegeterain)) {
            return spec.and(FoodSpecification.byVegetarian(true));
        }
        return spec;
    }


}