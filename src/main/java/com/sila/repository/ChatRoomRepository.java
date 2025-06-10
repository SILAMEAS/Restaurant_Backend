package com.sila.repository;

import com.sila.model.ChatRoom;
import com.sila.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findByRoomId(String roomId);

    Optional<ChatRoom> findById(Long id);

    Set<ChatRoom> findAllByMembers(Set<User> members);
}
