package br.com.gabriel.zlschat.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gabriel.zlschat.dtos.MessageDTO;
import br.com.gabriel.zlschat.exceptions.ChatNotFoundException;
import br.com.gabriel.zlschat.models.ChatRoom;
import br.com.gabriel.zlschat.models.Message;
import br.com.gabriel.zlschat.repositories.ChatRoomRepository;
import br.com.gabriel.zlschat.repositories.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public void send(MessageDTO messageDTO) {
        ChatRoom chatRoom = this.chatRoomRepository.findById(messageDTO.getChatRoomId())
            .orElseThrow(() -> new ChatNotFoundException("Chat não encontrado"));
        if (chatRoom.getUsersInChat().contains(messageDTO.getSenderUsername()) &&
            chatRoom.getUsersInChat().contains(messageDTO.getReceiverUsername())) {
            Message message = messageDTO.toEntity();
            this.messageRepository.save(message);
        } else {
            throw new ChatNotFoundException("Chat não encontrado");
        }
    }

    public List<Message> listMessages(UUID chatRoomId) {
        List<Message> messages = this.messageRepository.findByChatRoomId(chatRoomId);
        return messages;
    }
}
