package com.sila.dto;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ChatRoomMemberId implements Serializable {
    private Long chatRoomId;
    private Long userId;

    // equals() and hashCode() required
}