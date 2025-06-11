package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats-room")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping()
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(chatRoomService.createOrGet(senderId,receiverId));
    }

    @GetMapping()
    public ResponseEntity<EntityResponseHandler<ChatRoomResponse>> listAllRooms(@ModelAttribute PaginationRequest request) {
        return ResponseEntity.ok(chatRoomService.findAllByMember(request));
    }

    @Transactional
    @DeleteMapping("/bulk")
    public ResponseEntity<String> deleteAllRoom() {
        return ResponseEntity.ok(chatRoomService.deleteAllRoom());
    }

    @Transactional
    @DeleteMapping("/bulk/user")
    public ResponseEntity<String> deleteAllRoomByUser() {
        return ResponseEntity.ok(chatRoomService.deleteAllRoomByUser());
    }

}
