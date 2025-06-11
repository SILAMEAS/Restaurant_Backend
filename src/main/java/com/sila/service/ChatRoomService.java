package com.sila.service;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.model.ChatRoom;

import java.util.Optional;

public interface ChatRoomService {

    ChatRoomResponse createOrGet(Long senderId, Long receiverId);
    ChatRoom findById(Long chatRoomId);
    Optional<ChatRoom> findByRoomId(String roomId);

    String deleteAllRoom();

    String deleteAllRoomByUser();

    EntityResponseHandler<ChatRoomResponse> findAllByMember(PaginationRequest request);

    public static String generateRoom(Long senderId,Long receiverId){
        return senderId < receiverId
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;
    }

}
