package br.com.gabriel.zlschat.dtos;

import lombok.Data;

@Data
public class RequestFriendshipDTO {
    private String senderUsername;
    private String receiverUsername;
}
