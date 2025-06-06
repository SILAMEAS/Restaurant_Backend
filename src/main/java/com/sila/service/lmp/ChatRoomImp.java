package com.sila.service.lmp;

import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatRoomService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomImp implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    @Override
    public ChatRoom createOrGetChatRoom(Long senderId, Long receiverId) {
        // Generate consistent roomId format, e.g. user1-user2 or sorted
        String roomId = senderId < receiverId
                ? senderId + "-" + receiverId
                : receiverId + "-" + senderId;

        return chatRoomRepository.findByRoomId(roomId)
                .orElseGet(() -> {
                    ChatRoom room = new ChatRoom();
                    room.setRoomId(roomId);
                    room.setIsGroup(false);

                    var sender = userService.getById(senderId);
                    var receiver = userService.getById(receiverId);

                    room.setMembers(new HashSet<>(List.of(sender, receiver)));
                    return chatRoomRepository.save(room);
                });
    }
}
