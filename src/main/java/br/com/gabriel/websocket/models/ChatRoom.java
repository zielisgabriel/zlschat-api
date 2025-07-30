package br.com.gabriel.websocket.models;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import br.com.gabriel.websocket.enums.ChatRoomType;
import lombok.Data;

@Data
@Document(collection = "chatRooms")
public class ChatRoom {
    @Id
    private UUID id;
    private ChatRoomType chatRoomType;
    private List<String> usersInChat;
    private String name;

    public ChatRoom() {
        this.id = UUID.randomUUID();
    }
}
