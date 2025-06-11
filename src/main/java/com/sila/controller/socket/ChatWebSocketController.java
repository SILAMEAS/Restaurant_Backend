package com.sila.controller.socket;

import com.sila.dto.response.ChatMessageDTO;
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
public class ChatWebSocketController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/send")
    public void sendMessage(@Payload ChatMessageDTO dto) {
        User sender = userService.getById(dto.getSenderId());
        var users= dto.getRoomId().split("_");
        Long senderId = Long.parseLong(users[0]);
        Long receiverId = Long.parseLong(users[1]);
        String roomId = ChatRoomService.generateRoom(senderId,receiverId);

        ChatRoom room = chatRoomRepository.findByRoomId(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        // Explicitly send to the topic
        messagingTemplate.convertAndSend("/topic/messages", chatMessageService.createMessage(dto,room,sender));

    }
}