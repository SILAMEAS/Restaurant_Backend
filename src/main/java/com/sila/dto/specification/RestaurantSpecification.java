package com.sila.dto.specification;

import com.sila.model.Address_;
import com.sila.model.Food_;
import com.sila.model.Restaurant;
import com.sila.model.Restaurant_;
import org.springframework.data.jpa.domain.Specification;

public class RestaurantSpecification {
    public static Specification<Restaurant> likeNameOrDescription(String nameOrDescription){
        if(nameOrDescription==null){
            return null;
        }
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.or(
                criteriaBuilder.like(root.get(Restaurant_.NAME),nameOrDescription+"%"),
                criteriaBuilder.like(root.get(Restaurant_.DESCRIPTION),nameOrDescription+"%"),
                criteriaBuilder.like(root.get(Restaurant_.ADDRESS).get(Address_.STREET_ADDRESS),nameOrDescription+"%"),
                criteriaBuilder.like(root.get(Restaurant_.ADDRESS).get(Address_.POSTAL_CODE),nameOrDescription+"%"));

    }
    public static Specification<Restaurant> likeName(String name){
        if(name==null){
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(Restaurant_.NAME),"%"+name+"%");

    }

    public static Specification<Restaurant> likeDescription(String description){
        if(description==null){
            return null;
        }
        return (root, query, criteriaBuilder) ->criteriaBuilder.like(root.get(Restaurant_.DESCRIPTION),description+"%");

    }
    public static Specification<Restaurant> bySession(Boolean sessional){
        return (root, query, criteriaBuilder) ->criteriaBuilder.equal(root.get(Restaurant_.FOODS).get(
            Food_.IS_SEASONAL),sessional);

    }
    public static Specification<Restaurant> byVegetarian(Boolean vegetarian){
        return (root, query, criteriaBuilder) ->criteriaBuilder.equal(root.get(Restaurant_.FOODS).get(
            Food_.IS_VEGETARIAN),vegetarian);

    }

    public static Specification<Restaurant> likeCountry(String country){
        if(country==null){
            return null;
        }
        return (root, query, criteriaBuilder) ->criteriaBuilder.like(root.get(Restaurant_.ADDRESS).get(
            Address_.COUNTRY),"%"+country+"%");

    }
}
