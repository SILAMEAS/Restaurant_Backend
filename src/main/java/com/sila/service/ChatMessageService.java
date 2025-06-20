package com.sila.service;


import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.websocket.ChatMessageDTO;
import com.sila.model.ChatRoom;
import com.sila.model.User;

public interface ChatMessageService {

    ChatMessageDTO createMessage(ChatMessageDTO request,
                                 ChatRoom room, User sender);

    EntityResponseHandler<ChatMessageDTO> findAll(String roomId, PaginationRequest request);


}
