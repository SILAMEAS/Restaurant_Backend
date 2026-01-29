package com.sila.modules.cart.repository;

import com.sila.modules.cart.model.Cart;
import com.sila.modules.profile.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);

    List<Cart> findAllByUserId(Long userId);

    List<Cart> findAllByUser(User user);

    Optional<Cart> findByIdAndUserAndItemsNotEmpty(Long cardId, User user);


    @Query("DELETE FROM Cart c WHERE c.id = ?1")
    @Modifying
    void deleteByCardId(Long cardId);


    Optional<Object> findAByUser(User user);
}
