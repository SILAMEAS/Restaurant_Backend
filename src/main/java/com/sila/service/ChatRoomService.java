package com.sila.service;

import com.sila.model.ChatRoom;

public interface ChatRoomService {

    ChatRoom createOrGetChatRoom(Long senderId,Long receiverId);
}
