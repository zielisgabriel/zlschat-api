package br.com.gabriel.zlschat.dtos;

import org.hibernate.validator.constraints.UUID;

import lombok.Data;

@Data
public class RequestFriendshipDTO {
    @UUID
    private String senderUsername;
    @UUID
    private String receiverUsername;
}
