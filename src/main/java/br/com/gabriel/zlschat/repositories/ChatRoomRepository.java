package br.com.gabriel.zlschat.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.zlschat.models.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, UUID> {
    List<ChatRoom> findByUsersInChatContains(String username);
}
