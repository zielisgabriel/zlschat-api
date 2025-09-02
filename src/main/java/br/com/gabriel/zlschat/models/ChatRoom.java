package br.com.gabriel.zlschat.models;

import java.util.List;
import java.util.UUID;


import br.com.gabriel.zlschat.enums.ChatRoomType;
import lombok.Data;

@Data
public class ChatRoom {
    private UUID id;
    private ChatRoomType chatRoomType;
    private List<String> usersInChat;
    private String name;

    public ChatRoom() {
        this.id = UUID.randomUUID();
    }
}
