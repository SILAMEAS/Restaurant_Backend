package com.sila.controller;

import com.sila.dto.ChatMessageDTO;
import com.sila.model.ChatMessage;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatMessageRepository;
import com.sila.repository.ChatRoomRepository;
import com.sila.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat.send")
    @SendToUser("/messages")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO dto) {
        // Lookup or create ChatRoom
        ChatRoom room = chatRoomRepository.findByRoomId(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatMessage message = new ChatMessage();
        message.setContent(dto.getContent());
        message.setRoom(room);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);

        // Send to frontend
        return new ChatMessageDTO();
    }
}
