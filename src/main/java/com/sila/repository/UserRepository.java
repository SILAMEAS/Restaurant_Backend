package com.sila.repository;

import com.sila.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>, JpaSpecificationExecutor<User> {
    public User findByEmail(String username);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.favourites WHERE u.id = :userId")
    Optional<User> findByIdWithFavorites(@Param("userId") Long userId);
}
