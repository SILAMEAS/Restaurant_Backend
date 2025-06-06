package com.sila.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String content;
    private Long senderId;
    private String roomId;
}