package br.com.gabriel.zlschat.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "messages")
public class Message {
    private UUID id;
    private UUID chatRoomId;
    private String senderUsername;
    private String content;
    private LocalDateTime sendAt;

    public Message() {
        this.id = UUID.randomUUID();
        this.sendAt = LocalDateTime.now();
    }
}
