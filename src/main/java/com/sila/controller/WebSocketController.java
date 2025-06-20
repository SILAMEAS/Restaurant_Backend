package com.sila.controller;

import com.sila.dto.response.websocket.ChatMessageDTO;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatMessageService;
import com.sila.service.ChatRoomService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    // 🔹 Chat Message Handler
    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageDTO dto) {
        User sender = userService.getById(dto.getSenderId());
        String roomId = ChatRoomService.generateRoom(dto.getRoomId());

        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        // Explicitly send to the topic
        messagingTemplate.convertAndSend("/topic/messages", chatMessageService.createMessage(dto, room, sender));

    }
    // 🔹 Notification Handler
//    @MessageMapping("/notification/send")
//    public void sendNotification(@Payload NotificationDTO dto) {
//        messagingTemplate.convertAndSend("/topic/notification/" +dto);
//    }
//
//    // 🔹 Alert Broadcast
//    @MessageMapping("/alert/broadcast")
//    public void sendAlert(@Payload AlertDTO dto) {
//        messagingTemplate.convertAndSend("/topic/alert", dto);
//    }
}
