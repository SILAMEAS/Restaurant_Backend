package com.sila.specifcation;

import com.sila.model.Address_;
import com.sila.model.User;
import com.sila.model.User_;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSpecification {
    public static Specification<User> search(String search) {
        if (search == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get(User_.fullName), search + "%"),
                criteriaBuilder.like(root.get(User_.email), search + "%"),
                criteriaBuilder.like(root.get(User_.ADDRESSES).get(Address_.STREET), search + "%")
        );

    }
}