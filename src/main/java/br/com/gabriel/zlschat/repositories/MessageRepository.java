package br.com.gabriel.zlschat.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.zlschat.models.Message;

public interface MessageRepository extends MongoRepository<Message, UUID> {
    List<Message> findByChatRoomId(UUID chatRoomId);
    Optional<Message> findById(UUID id);
    boolean existsByTempIdAndChatRoomId(UUID tempId, UUID chatRoomId);
    boolean existsByIdAndSenderUsername(UUID id, String senderUsername);
}
