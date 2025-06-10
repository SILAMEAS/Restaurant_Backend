package com.sila.controller;

import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.ChatMessageDTO;
import com.sila.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/chats-message")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<EntityResponseHandler<ChatMessageDTO>> listMessage(@PathVariable String roomId, @ModelAttribute PaginationRequest request) {
        return new ResponseEntity<>(chatMessageService.findAll(roomId,request), HttpStatus.OK);
    }
}
