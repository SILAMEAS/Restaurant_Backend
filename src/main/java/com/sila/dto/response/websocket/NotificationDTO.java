package com.sila.dto.response.websocket;

public class NotificationDTO {
    private Long receiverId;
    private String content;
    private String type; // e.g., "NEW_MESSAGE", "FRIEND_REQUEST"
}