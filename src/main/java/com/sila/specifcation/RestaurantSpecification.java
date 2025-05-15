package com.sila.specifcation;

import com.sila.dto.request.SearchRequest;
import com.sila.model.Address_;
import com.sila.model.Food_;
import com.sila.model.Restaurant;
import com.sila.model.Restaurant_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)

public class RestaurantSpecification {
    public static Specification<Restaurant> likeNameOrDescription(String nameOrDescription) {
        if (nameOrDescription == null) {
            return null;
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(root.get(Restaurant_.NAME), nameOrDescription + "%"),
                        criteriaBuilder.like(root.get(Restaurant_.DESCRIPTION), nameOrDescription + "%"),
                        criteriaBuilder.like(root.get(Restaurant_.ADDRESS).get(Address_.STREET), nameOrDescription + "%"),
                        criteriaBuilder.like(root.get(Restaurant_.ADDRESS).get(Address_.ZIP), nameOrDescription + "%"));

    }

    public static Specification<Restaurant> likeName(String name) {
        if (name == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Restaurant_.NAME), "%" + name + "%");

    }

    public static Specification<Restaurant> likeDescription(String description) {
        if (description == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Restaurant_.DESCRIPTION), description + "%");

    }

    public static Specification<Restaurant> bySession(Boolean sessional) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Restaurant_.FOODS).get(
                Food_.IS_SEASONAL), sessional);

    }

    public static Specification<Restaurant> byVegetarian(Boolean vegetarian) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Restaurant_.FOODS).get(
                Food_.IS_VEGETARIAN), vegetarian);

    }

    public static Specification<Restaurant> likeCountry(String country) {
        if (country == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Restaurant_.ADDRESS).get(
                Address_.COUNTRY), "%" + country + "%");

    }
    public static Specification<Restaurant> filterRestaurant(SearchRequest searchReq) {
        Specification<Restaurant> spec = Specification.where(null);
        if (Objects.nonNull(searchReq.getSearch())) {
            spec = spec.and(RestaurantSpecification.likeNameOrDescription(searchReq.getSearch()));
        }
        return spec;
    }
}
