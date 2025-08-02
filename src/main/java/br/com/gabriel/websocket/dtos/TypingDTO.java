package br.com.gabriel.websocket.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class TypingDTO {
    private boolean typing;
    private UUID chatRoomId;
    private String username;
}
