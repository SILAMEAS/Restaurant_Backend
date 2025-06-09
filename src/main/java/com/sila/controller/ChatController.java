package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.ChatMessageDTO;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.repository.UserRepository;
import com.sila.service.ChatMessageService;
import com.sila.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final UserRepository userRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(@RequestParam Long senderId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(chatRoomService.createOrGet(senderId,receiverId));
    }

    @GetMapping("/rooms")
    public ResponseEntity<EntityResponseHandler<ChatRoomResponse>> listRooms() {
        return new ResponseEntity<>(chatRoomService.findRoomAll(), HttpStatus.OK);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<EntityResponseHandler<ChatMessageDTO>> listMessage(@PathVariable String roomId, @ModelAttribute PaginationRequest request) {
        return new ResponseEntity<>(chatMessageService.findAll(roomId,request), HttpStatus.OK);
    }
    @DeleteMapping("")
    public ResponseEntity<String> delete() {
        return ResponseEntity.ok("Delete success");

    }


}
