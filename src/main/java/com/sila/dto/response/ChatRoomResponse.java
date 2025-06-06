package com.sila.dto.response;

import com.sila.model.ChatRoom;
import com.sila.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoomResponse {
    private Long id;

    private String roomId; // e.g., "group-123" or "user1-user2"

    private Boolean isGroup = false;
    private String groupName;

    private Set<UserResponse.UserResponseCustom> members;

    public static ChatRoomResponse toResponse(ChatRoom chatRoom){
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .isGroup(chatRoom.getIsGroup())
                .groupName(chatRoom.getGroupName())
                .roomId(chatRoom.getRoomId())
                .groupName(chatRoom.getGroupName())
                .members(chatRoom.getMembers().stream().map(UserResponse::toUserResponseCustom).collect(Collectors.toSet()))
                .build();
    }
}
