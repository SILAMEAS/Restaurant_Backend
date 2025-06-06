package com.sila.controller;

import com.sila.dto.response.ChatRoomResponse;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatRoomRepository;
import com.sila.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chat-room")
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    @PostMapping
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(@RequestParam Long senderId, @RequestParam Long receiverId) {
        String roomId = senderId < receiverId
                ? senderId + "-" + receiverId
                : receiverId + "-" + senderId;

        Optional<ChatRoom> existingRoom = chatRoomRepository.findByRoomId(roomId);
        if (existingRoom.isPresent()) {
            var roomExit = existingRoom.get();
            return ResponseEntity.ok(ChatRoomResponse.toResponse(roomExit));
        }

        ChatRoom room = new ChatRoom();
        room.setRoomId(roomId);
        room.setIsGroup(false);

        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        room.setMembers(Set.of(sender, receiver));

        try {
            ChatRoom saved = chatRoomRepository.save(room);
            return ResponseEntity.ok(ChatRoomResponse.toResponse((saved)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
