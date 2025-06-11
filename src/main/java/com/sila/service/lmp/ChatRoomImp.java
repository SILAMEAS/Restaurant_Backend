package com.sila.service.lmp;

import com.sila.config.context.UserContext;
import com.sila.dto.EntityResponseHandler;
import com.sila.dto.request.PaginationRequest;
import com.sila.dto.response.ChatRoomResponse;
import com.sila.exception.NotFoundException;
import com.sila.model.ChatRoom;
import com.sila.model.User;
import com.sila.repository.ChatMessageRepository;
import com.sila.repository.ChatRoomRepository;
import com.sila.repository.UserRepository;
import com.sila.service.ChatRoomService;
import com.sila.service.UserService;
import com.sila.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatRoomImp implements ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final UserRepository userRepository;

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
    public EntityResponseHandler<ChatRoomResponse> findAllByMember(PaginationRequest request) {
        var user = UserContext.getUser();
        var chatRooms = chatRoomRepository.findAllByMembers(PageableUtil.fromRequest(request),Set.of(user));
        return new EntityResponseHandler<>(chatRooms.map(ChatRoomResponse::toResponse));
    }

    @Override
    public ChatRoomResponse createOrGet(Long senderId,Long receiverId) {
        String roomId = ChatRoomService.generateRoom(senderId+"_"+receiverId);

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
