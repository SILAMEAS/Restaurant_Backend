package com.sila.controller;

import com.sila.config.context.UserContext;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.model.ChatRoom;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;
    private final ModelMapper modelMapper;
    @PostMapping()
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(chatRoomService.createOrGet(senderId,receiverId));
    }

    @GetMapping()
    public ResponseEntity<?> listAllRooms() {
        var user = UserContext.getUser();
        var rooms = chatRoomRepository.findAllByMembers(Set.of(user));
        return ResponseEntity.ok(rooms.stream().map(ChatRoomResponse::toResponse).toList());
    }



}
