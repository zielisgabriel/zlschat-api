package br.com.gabriel.zlschat.services;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import br.com.gabriel.zlschat.config.RabbitConfig;
import br.com.gabriel.zlschat.dtos.MessageDTO;
import br.com.gabriel.zlschat.dtos.ReadMessageStatusDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProducerService {
    private final RabbitTemplate rabbitTemplate;

    public void publishMessage(MessageDTO messageDTO) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.CHAT_QUEUE, messageDTO);
    }

    public void publishStatusMessage(ReadMessageStatusDTO readMessageStatusDTO) {
        this.rabbitTemplate.convertAndSend(RabbitConfig.STATUS_QUEUE, readMessageStatusDTO);
    }
}
