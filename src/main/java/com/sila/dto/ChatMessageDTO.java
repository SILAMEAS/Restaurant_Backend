package com.sila.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private String content;
    private Long senderId;
    private String roomId;
    private LocalDateTime timestamp;
    private String senderName;  // Added this field

}