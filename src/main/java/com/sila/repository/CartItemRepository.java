package com.sila.repository;

import com.sila.model.Cart;
import com.sila.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByFood_Id(Long foodId);

    Optional<CartItem> findByCart(Cart cart);
}
