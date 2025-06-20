package com.sila.dto.response.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO implements Serializable {
    private Long id;
    private String content;
    private Long senderId;
    private String roomId;
    private Date timestamp;
    private String senderName;  // Added this field

}