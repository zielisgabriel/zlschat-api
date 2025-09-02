package br.com.gabriel.zlschat.services;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import br.com.gabriel.zlschat.config.RabbitConfig;
import br.com.gabriel.zlschat.dtos.MessageDTO;
import br.com.gabriel.zlschat.dtos.ReadMessageStatusDTO;
import br.com.gabriel.zlschat.models.Message;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @RabbitListener(queues = RabbitConfig.CHAT_QUEUE)
    public void consumerMessage(MessageDTO messageDTO) {
        Message messageSent = this.messageService.saveMessage(messageDTO);

        this.simpMessagingTemplate.convertAndSendToUser(
            messageDTO.getReceiverUsername(),
            "/queue/messages",
            messageSent
        );

        this.simpMessagingTemplate.convertAndSendToUser(
            messageDTO.getSenderUsername(),
            "/queue/messages",
            messageSent
        );
    }

    @RabbitListener(queues = RabbitConfig.STATUS_QUEUE)
    public void consumerStatusMessage(ReadMessageStatusDTO readMessageStatusDTO) {
        UUID messageId = UUID.fromString(readMessageStatusDTO.getMessageIdString());
        Message message = this.messageService.markMessageAsRead(messageId, readMessageStatusDTO.getSenderUsername());
        this.simpMessagingTemplate.convertAndSendToUser(
            message.getSenderUsername(),
            "/queue/message-status",
            message
        );
    }
}
