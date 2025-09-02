package br.com.gabriel.zlschat.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import br.com.gabriel.zlschat.dtos.MessageDTO;
import br.com.gabriel.zlschat.models.ChatRoom;
import br.com.gabriel.zlschat.models.Message;
import br.com.gabriel.zlschat.repositories.ChatRoomRepository;
import br.com.gabriel.zlschat.repositories.MessageRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private MessageService messageService;

    @Test
    @DisplayName("Should send message")
    public void sendSuccess() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setId(UUID.randomUUID());
        chatRoom.setUsersInChat(List.of("user1", "user2"));

        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setChatRoomId(chatRoom.getId());
        messageDTO.setSenderUsername("user1");
        messageDTO.setReceiverUsername("user2");
        messageDTO.setContent("Hello");

        when(this.chatRoomRepository.findById(messageDTO.getChatRoomId())).thenReturn(Optional.of(chatRoom));
        
        this.messageService.saveMessage(messageDTO);
        
        ArgumentCaptor<Message> argumentCaptorMessage = ArgumentCaptor.forClass(Message.class);
        verify(this.messageRepository).save(argumentCaptorMessage.capture());
    }
}
