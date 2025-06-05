package com.sila.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String content;
    private Long senderId;
    private Long receiverId;
    private String roomId;
}