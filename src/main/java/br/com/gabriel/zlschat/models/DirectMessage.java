package br.com.gabriel.zlschat.models;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.lang.NonNull;

import lombok.Data;

@Data
@Document(collection = "direct_messages")
public class DirectMessage {
    @Id
    private String id;
    @NonNull
    private String senderUsername;
    @NonNull
    private String receiverUsername;

    public DirectMessage() {
        this.id = UUID.randomUUID().toString();
    }
}
