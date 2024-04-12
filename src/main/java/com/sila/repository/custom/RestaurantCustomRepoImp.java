package com.sila.repository.custom;

import com.sila.model.Restaurant;
import com.sila.model.Restaurant_;
import com.sila.repository.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;

@RequiredArgsConstructor
public class RestaurantCustomRepoImp implements RestaurantCustomRepo{
    private final EntityManager entityManager;


    @Override
    public List<Restaurant> findByNameAndDescription(String name, String description) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery();
        Root<Restaurant> restaurantRoot= cq.from(Restaurant.class);
        Predicate namePredicate = cb.equal(restaurantRoot.get(Restaurant_.NAME),restaurantRoot);
        Predicate descriptionPredicate = cb.equal(restaurantRoot.get(Restaurant_.DESCRIPTION),restaurantRoot);
        cq.where(namePredicate,descriptionPredicate);
        TypedQuery<Restaurant> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
