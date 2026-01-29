package com.sila.modules.chat.services;


import com.sila.share.pagination.EntityResponseHandler;
import com.sila.share.dto.req.PaginationRequest;
import com.sila.modules.chat.dto.ChatMessageDTO;

public interface ChatMessageService {

    ChatMessageDTO createMessage(ChatMessageDTO request);

    EntityResponseHandler<ChatMessageDTO> findAll(String roomId, PaginationRequest request);


}
