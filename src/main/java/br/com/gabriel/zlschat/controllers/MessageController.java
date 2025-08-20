package br.com.gabriel.zlschat.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.zlschat.dtos.MessageDTO;
import br.com.gabriel.zlschat.dtos.TypingDTO;
import br.com.gabriel.zlschat.models.Message;
import br.com.gabriel.zlschat.services.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send.message")
    public void send(@Payload MessageDTO messageDTO) {
        this.messageService.send(messageDTO);
        this.simpMessagingTemplate.convertAndSendToUser(messageDTO.getReceiverUsername(), "/queue/messages", messageDTO);
    }

    @MessageMapping("/typing")
    public void typing(@Payload TypingDTO typingDTO) {
        this.simpMessagingTemplate.convertAndSend("/topic/typing/" + typingDTO.getChatRoomId(), typingDTO);
    }

    @GetMapping("/api/messages/{chatRoomId}")
    public ResponseEntity<List<Message>> listMessages(@PathVariable UUID chatRoomId) {
        List<Message> messages = this.messageService.listMessages(chatRoomId);
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }
}
