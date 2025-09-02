package br.com.gabriel.zlschat.models;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.gabriel.zlschat.enums.MessageStatus;
import lombok.Data;
import lombok.NonNull;

@Data
public class Message {
    private UUID id;
    
    @NonNull
    private UUID tempId;

    @NonNull
    private UUID chatRoomId;

    @NonNull
    private String senderUsername;

    @NonNull
    private String receiverUsername;

    @NonNull
    private String content;

    private MessageStatus status;
    private LocalDateTime sendAt;

    public Message() {
        this.id = UUID.randomUUID();
        this.sendAt = LocalDateTime.now();
        this.status = MessageStatus.SENT;
    }
}
