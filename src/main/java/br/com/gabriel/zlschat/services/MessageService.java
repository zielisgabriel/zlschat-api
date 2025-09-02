package br.com.gabriel.zlschat.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.gabriel.zlschat.dtos.MessageDTO;
import br.com.gabriel.zlschat.enums.MessageStatus;
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

    public List<Message> listMessages(UUID chatRoomId) {
        return this.messageRepository.findByChatRoomId(chatRoomId);
    }

    public Message saveMessage(MessageDTO messageDTO) {
        if (messageDTO.getStatus() != MessageStatus.SENDING) {
            throw new RuntimeException("Houve uma modificação no código fonte do front-end!");
        }

        boolean isExistsSameMessageInChat = this.messageRepository.existsByTempIdAndChatRoomId(
            messageDTO.getTempId(), messageDTO.getChatRoomId());
        if (isExistsSameMessageInChat) {
            throw new RuntimeException("Mensagem já existe no chat");
        }

        ChatRoom chatRoom = this.chatRoomRepository.findById(messageDTO.getChatRoomId())
            .orElseThrow(() -> new ChatNotFoundException("Chat não encontrado"));

        if (chatRoom.getUsersInChat().contains(messageDTO.getSenderUsername()) &&
            chatRoom.getUsersInChat().contains(messageDTO.getReceiverUsername())) {
            Message msg = messageDTO.toEntity();
            msg.setStatus(MessageStatus.SENT);
            this.messageRepository.save(msg);
            return msg;
        } else {
            throw new ChatNotFoundException("Chat não encontrado");
        }
    }

    public Message markMessageAsRead(UUID messageId, String senderUsername) {
        boolean isMessageValid = this.messageRepository.existsByIdAndSenderUsername(messageId, senderUsername);
        if (!isMessageValid) {
            throw new RuntimeException("Mensagem não encontrada ou não pertence ao usuário");
        }
        Message message = this.updateMessageStatus(messageId, MessageStatus.READ);
        return message;
    }

    private Message updateMessageStatus(UUID messageId, MessageStatus status) {
        Message message = this.messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Mensagem não encontrada"));
        message.setStatus(status);
        return this.messageRepository.save(message);
    }
}
