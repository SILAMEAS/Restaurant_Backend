package com.sila.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO implements Serializable {
    private Long id;
    private String content;
    private Long senderId;
    private String roomId;
    private LocalDateTime timestamp;
    private String senderName;  // Added this field

}