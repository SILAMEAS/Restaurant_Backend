package com.sila.repository;

import com.sila.model.ChatMessage;
import com.sila.model.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoom_RoomIdOrderByTimestampAsc(String roomId);
    Page<ChatMessage> findAllByRoom(Pageable pageable, ChatRoom room);

    void deleteByRoom(ChatRoom room);
}
