package br.com.gabriel.zlschat.dtos;

import java.util.UUID;

import br.com.gabriel.zlschat.enums.MessageStatus;
import br.com.gabriel.zlschat.models.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageDTO {
    @NotNull
    private UUID tempId;
    @NotNull
    private UUID chatRoomId;
    @NotNull
    private String senderUsername;
    @NotNull
    private String receiverUsername;
    @NotNull
    @NotBlank
    private String content;
    private MessageStatus status;

    public Message toEntity() {
        Message message = new Message();
        message.setTempId(this.tempId);
        message.setChatRoomId(this.chatRoomId);
        message.setSenderUsername(this.senderUsername);
        message.setReceiverUsername(this.receiverUsername);
        message.setContent(this.content);
        message.setStatus(this.status);
        return message;
    }
}
