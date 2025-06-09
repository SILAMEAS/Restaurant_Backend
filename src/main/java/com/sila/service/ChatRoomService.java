package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomService {

    ChatRoomResponse createOrGet(Long senderId,Long receiverId);
    ChatRoom findById(Long chatRoomId);

    EntityResponseHandler<ChatRoomResponse> findRoomAll();

    Optional<ChatRoom> findByRoomId(String roomId);


}
