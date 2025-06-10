package com.sila.controller.socket;

import com.sila.dto.response.ChatMessageDTO;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatMessageService;
import com.sila.service.UserService;
import com.sila.service.lmp.ChatRoomImp;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageDTO dto) {
        User sender = userService.getById(dto.getSenderId());
        ChatRoom room = chatRoomRepository.findByRoomId(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        // Explicitly send to the topic
        messagingTemplate.convertAndSend("/topic/messages", chatMessageService.createMessage(dto,room,sender));

    }
}