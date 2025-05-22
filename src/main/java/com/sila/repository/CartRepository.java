package com.sila.repository;

import com.sila.model.Cart;
import com.sila.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    Optional<Cart> findAllByUserId(Long userId);
}
