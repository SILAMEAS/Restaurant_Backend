package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.exception.NotFoundException;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatMessageRepository;
import com.sila.repository.ChatRoomRepository;
import com.sila.service.ChatRoomService;
import com.sila.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomImp implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    @Override
    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(()->new NotFoundException("not found room with this id"));
    }


    @Override
    public Optional<ChatRoom> findByRoomId(String roomId) {
        return  chatRoomRepository.findByRoomId(roomId);
    }

    @Override
    public String deleteAllRoom() {

        var rooms = chatRoomRepository.findAll();

        // Delete messages first
        for (ChatRoom room : rooms) {
            chatMessageRepository.deleteByRoom(room);
        }

        // Then delete rooms
        chatRoomRepository.deleteAll(rooms);

        return "Deleted all rooms";

    }

    @Override
    public String deleteAllRoomByUser() {
        var user = UserContext.getUser();
        var rooms = chatRoomRepository.findAllByMembers(Set.of(user));

        // Delete messages first
        for (ChatRoom room : rooms) {
            chatMessageRepository.deleteByRoom(room);
        }

        // Then delete rooms
        chatRoomRepository.deleteAll(rooms);

        return "Deleted all rooms";

    }

    @Override
    public ChatRoomResponse createOrGet(Long senderId,Long receiverId) {
        String roomId = senderId < receiverId
                ? senderId + "_" + receiverId
                : receiverId + "_" + senderId;

        Optional<ChatRoom> existingRoom = findByRoomId(roomId);
        if (existingRoom.isPresent()) {
            var roomExit = existingRoom.get();
            return ChatRoomResponse.toResponse(roomExit);
        }

        ChatRoom room = new ChatRoom();
        room.setRoomId(roomId);
        room.setIsGroup(false);

        User sender = userService.getById(senderId);
        User receiver = userService.getById(receiverId);
        room.setMembers(Set.of(sender, receiver));

        ChatRoom saved = chatRoomRepository.save(room);
        return ChatRoomResponse.toResponse(saved);
    }

}
