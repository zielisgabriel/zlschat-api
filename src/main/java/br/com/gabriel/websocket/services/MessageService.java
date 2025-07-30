package br.com.gabriel.websocket.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gabriel.websocket.dtos.MessageDTO;
import br.com.gabriel.websocket.exceptions.ChatNotFoundException;
import br.com.gabriel.websocket.models.ChatRoom;
import br.com.gabriel.websocket.models.Message;
import br.com.gabriel.websocket.repositories.ChatRoomRepository;
import br.com.gabriel.websocket.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void send(MessageDTO messageDTO) {
        List<ChatRoom> chatRooms = this.chatRoomRepository.findByUsersInChatContains(messageDTO.getSenderUsername());
        for (ChatRoom chatRoom : chatRooms) {
            if (!chatRoom.getUsersInChat().contains(messageDTO.getReceiverUsername().toString())) {
                throw new ChatNotFoundException("Chat não encontrado");
            }
        }
        Message message = messageDTO.toEntity();
        this.messageRepository.save(message);
    }

    public List<Message> listMessages(UUID chatRoomId) {
        List<Message> messages = this.messageRepository.findByChatRoomId(chatRoomId);
        return messages;
    }
}
