package br.com.gabriel.zlschat.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.zlschat.models.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, UUID> {
    boolean existsByUsersInChatContains(String username);
    List<ChatRoom> findByUsersInChatContains(String username);
    Optional<ChatRoom> findById(UUID id);
}
