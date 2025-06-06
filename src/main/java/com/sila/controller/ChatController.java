package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.ChatMessageDTO;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatRoomRepository;
import com.sila.repository.UserRepository;
import com.sila.service.ChatMessageService;
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

import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageService chatMessageService;
    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponse> createOrGetRoom(@RequestParam Long senderId, @RequestParam Long receiverId) {
        String roomId = senderId < receiverId
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;

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

        ChatRoom saved = chatRoomRepository.save(room);
        return ResponseEntity.ok(ChatRoomResponse.toResponse((saved)));
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
