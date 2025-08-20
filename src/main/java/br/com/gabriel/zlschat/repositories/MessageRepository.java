package br.com.gabriel.zlschat.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import br.com.gabriel.zlschat.models.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatRoomId(UUID chatRoomId);
}
