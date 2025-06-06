package com.sila.controller.socket;

import com.sila.dto.ChatMessageDTO;
import com.sila.model.ChatMessage;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatMessageRepository;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatRoomService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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

    @MessageMapping("/chat/send")
//    @SendTo("/topic/messages")  // Changed this line
    public void sendMessage(@Payload ChatMessageDTO dto) {
        User sender = userService.getById(dto.getSenderId());
        ChatRoom room = chatRoomRepository.findByRoomId(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Chat room not found"));

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
        responseDto.setTimestamp(message.getTimestamp());
        responseDto.setSenderName(sender.getFullName()); // Add sender name if available

        // Explicitly send to the topic
        messagingTemplate.convertAndSend("/topic/messages", responseDto);

    }
}