package com.sila.repository;

import com.sila.model.Cart;
import com.sila.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByFood_Id(Long foodId);

    Optional<CartItem> findByCart(Cart cart);

    Optional<CartItem> findByIdAndCart(Long id, Cart cart);

    void deleteCartItemByCart(Cart cart);


    @Query("DELETE FROM CartItem ct WHERE ct.cart.id = ?1")
    @Modifying
    void deleteAllByCartId(Long cardId);

    Object findAllByCart(Cart cart);

    Iterable<Long> id(Long id);
}
