package com.sila.repository.custom;

import com.sila.model.Restaurant;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface RestaurantCustomRepo {

    List<Restaurant> findByNameAndDescription(String name, String description);
}
