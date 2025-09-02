package br.com.gabriel.zlschat.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.zlschat.models.ChatRoom;
import br.com.gabriel.zlschat.services.ChatRoomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<ChatRoom> listChatRooms() {
        List<ChatRoom> chatRooms = this.chatRoomService.listChatRooms();
        return chatRooms;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/{chatRoomId}")
    public ChatRoom findChatRoomById(@PathVariable UUID chatRoomId) {
        ChatRoom chatRoom = this.chatRoomService.findChatRoomById(chatRoomId);
        return chatRoom;
    }
}
