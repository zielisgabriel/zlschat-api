package br.com.gabriel.zlschat.dtos;

import lombok.Data;

@Data
public class ReadMessageStatusDTO {
    private String messageIdString;
    private String senderUsername;
}
