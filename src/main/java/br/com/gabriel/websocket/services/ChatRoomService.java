package br.com.gabriel.websocket.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.gabriel.websocket.enums.ChatRoomType;
import br.com.gabriel.websocket.models.ChatRoom;
import br.com.gabriel.websocket.models.UserEntity;
import br.com.gabriel.websocket.repositories.ChatRoomRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    @Autowired
    private UserService userService;
    private final ChatRoomRepository chatRoomRepository;

    public void createPrivateChatRoom(String username) {
        UserEntity authenticatedUser = this.userService.me();
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setChatRoomType(ChatRoomType.PRIVATE);
        chatRoom.setUsersInChat(List.of(authenticatedUser.getUsername(), username));
        this.chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoom> listChatRooms() {
        UserEntity authenticatedUser = this.userService.me();
        return this.chatRoomRepository.findByUsersInChatContains(authenticatedUser.getUsername());
    }

    public ChatRoom findChatRoomById(UUID chatRoomId) {
        ChatRoom chatRoom = this.chatRoomRepository.findById(chatRoomId)
            .orElseThrow(() -> new RuntimeException("ChatRoom nao encontrado"));
        return chatRoom;
    }
}
