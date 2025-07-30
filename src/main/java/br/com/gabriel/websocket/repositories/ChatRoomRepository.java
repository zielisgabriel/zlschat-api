package br.com.gabriel.websocket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.websocket.models.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, UUID> {
    List<ChatRoom> findByUsersInChatContains(String username);
}
