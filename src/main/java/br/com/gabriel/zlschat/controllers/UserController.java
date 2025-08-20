package br.com.gabriel.zlschat.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.zlschat.dtos.RequestFriendshipDTO;
import br.com.gabriel.zlschat.dtos.UserDTO;
import br.com.gabriel.zlschat.models.UserEntity;
import br.com.gabriel.zlschat.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDTO userDTO) {
        this.userService.register(userDTO);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<List<UserEntity>> findByUsername(@PathVariable String username) {
        List<UserEntity> users = this.userService.findByUsername(username);
        return new ResponseEntity<List<UserEntity>>(users, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserEntity> me() {
        UserEntity user = this.userService.me();
        return new ResponseEntity<UserEntity>(user, HttpStatus.OK);
    }

    @MessageMapping("/request.friendship")
    public void requestFriendship(@Payload RequestFriendshipDTO requestFriendshipDTO) {
        String senderUsername = requestFriendshipDTO.getSenderUsername();
        String receiverUsername = requestFriendshipDTO.getReceiverUsername();
        
        this.userService.requestFriendship(receiverUsername);
        this.simpMessagingTemplate.convertAndSendToUser(receiverUsername, "/queue/friendship.request", senderUsername);
    }

    @PostMapping("/friends/accept/{senderUsername}")
    public ResponseEntity<Void> acceptFriendship(@PathVariable String senderUsername) {
        this.userService.acceptFriendship(senderUsername);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/friends/reject/{senderUsername}")
    public ResponseEntity<Void> rejectFriendship(@PathVariable String senderUsername) {
        this.userService.rejectFriendship(senderUsername);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
