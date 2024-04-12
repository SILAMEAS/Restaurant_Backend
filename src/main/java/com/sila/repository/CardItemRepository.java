package com.sila.repository;

import com.sila.model.Card;
import com.sila.model.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardItemRepository extends JpaRepository<CardItem,Long> {
    public List<CardItem> findByCardId(Long card_id);

}
