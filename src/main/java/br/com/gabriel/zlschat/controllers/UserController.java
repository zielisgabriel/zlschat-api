package br.com.gabriel.zlschat.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.zlschat.config.AuthenticationUserProvider;
import br.com.gabriel.zlschat.dtos.FriendDTO;
import br.com.gabriel.zlschat.dtos.UserDTO;
import br.com.gabriel.zlschat.dtos.UserStatusDTO;
import br.com.gabriel.zlschat.models.UserEntity;
import br.com.gabriel.zlschat.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public void register(@Valid @RequestBody UserDTO userDTO) {
        this.userService.register(userDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/find/{username}")
    public List<UserEntity> findByUsername(@PathVariable String username) {
        List<UserEntity> users = this.userService.findByUsername(username);
        return users;
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserEntity me() {
        UserEntity user = this.userService.me();
        return user;
    }

    @PostMapping("/friends/request/{receiverUsername}")
    @ResponseStatus(HttpStatus.OK)
    public void requestFriendship(@PathVariable String receiverUsername) {
        this.userService.requestFriendship(receiverUsername);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/friends/accept/{senderUsername}")
    public void acceptFriendship(@PathVariable String senderUsername) {
        this.userService.acceptFriendship(senderUsername);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/friends/reject/{senderUsername}")
    public void rejectFriendship(@PathVariable String senderUsername) {
        this.userService.rejectFriendship(senderUsername);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/friends")
    public List<FriendDTO> getFriends() {
        AuthenticationUserProvider authenticationUserProvider = new AuthenticationUserProvider();
        return this.userService.getMyFriends(authenticationUserProvider.getUsernameOfAuthenticatedUser().toString());
    }

    @MessageMapping("/user.status")
    public void updateStatus(UserStatusDTO userStatusDTO, Principal principal) {
        if (!userStatusDTO.getUsername().equals(principal.getName())) {
            throw new RuntimeException("Você não pode atualizar o status de outro usuário");
        }
        this.userService.updateStatus(userStatusDTO);
    }
}
