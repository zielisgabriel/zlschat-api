package br.com.gabriel.websocket.dtos;

import java.util.UUID;

import br.com.gabriel.websocket.models.Message;
import lombok.Data;

@Data
public class MessageDTO {
    private UUID chatRoomId;
    private String senderUsername;
    private String receiverUsername;
    private String content;

    public Message toEntity() {
        Message message = new Message();
        message.setChatRoomId(this.chatRoomId);
        message.setSenderUsername(this.senderUsername);
        message.setContent(this.content);
        return message;
    }
}
