package com.sila.controller;

import com.sila.dto.ChatMessageDTO;
import com.sila.model.ChatMessage;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatMessageRepository;
import com.sila.repository.ChatRoomRepository;
import com.sila.repository.UserRepository;
import com.sila.service.ChatRoomService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat.send")
    @SendToUser("/messages")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO dto) {
        User sender = userService.getById(dto.getSenderId());

        ChatRoom room;

        // Check if roomId is null or empty — this means it's the first message
        if (dto.getRoomId() == null || dto.getRoomId().isEmpty()) {
            // You must determine the receiver somehow, maybe infer it from WebSocket session or attach manually
            throw new RuntimeException("Room ID is required for sending a message. If it's a new chat, use a separate endpoint to create a room first.");
        } else {
            // Room already exists — proceed as normal
            room = chatRoomRepository.findByRoomId(dto.getRoomId())
                    .orElseThrow(() -> new RuntimeException("Chat room not found"));
        }

        // Save the message
        ChatMessage message = new ChatMessage();
        message.setContent(dto.getContent());
        message.setRoom(room);
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());

        chatMessageRepository.save(message);

        // Return response DTO
        ChatMessageDTO responseDto = new ChatMessageDTO();
        responseDto.setContent(message.getContent());
        responseDto.setSenderId(sender.getId());
        responseDto.setRoomId(room.getRoomId());

        return responseDto;
    }
}
