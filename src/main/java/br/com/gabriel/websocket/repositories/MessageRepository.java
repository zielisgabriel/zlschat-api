package br.com.gabriel.websocket.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.websocket.models.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatRoomId(UUID chatRoomId);
}
