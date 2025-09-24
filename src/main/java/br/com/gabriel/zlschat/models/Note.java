package br.com.gabriel.zlschat.models;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "notes")
public class Note {
    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;
    private String content;
    @Indexed(expireAfter = "10m")
    private Instant createdAt;

    public Note() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Instant.now();
    }
}
