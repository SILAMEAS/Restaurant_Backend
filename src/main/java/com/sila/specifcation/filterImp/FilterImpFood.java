package com.sila.specifcation.filterImp;

import com.sila.dto.request.SearchRequest;
import com.sila.model.Food;
import com.sila.repository.AddressRepository;
import com.sila.service.UserService;
import com.sila.specifcation.FoodSpecification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilterImpFood {
    private AddressRepository addressRepository;
    private UserService userService;

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
