package com.sila.dto.response;

import com.sila.model.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String name;

    private Set<UserResponse.UserResponseCustom> members;

    public static ChatRoomResponse toResponse(ChatRoom chatRoom){
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .isGroup(chatRoom.getIsGroup())
                .name(chatRoom.getGroupName()!=null?chatRoom.getGroupName():"Chat"+chatRoom.getId())
                .roomId(chatRoom.getRoomId())
                .members(chatRoom.getMembers().stream().map(UserResponse::toUserResponseCustom).collect(Collectors.toSet()))
                .build();
    }
}
