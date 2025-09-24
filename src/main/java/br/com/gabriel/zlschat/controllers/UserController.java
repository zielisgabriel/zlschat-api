package br.com.gabriel.zlschat.controllers;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.zlschat.dtos.RegisterCredentialsDTO;
import br.com.gabriel.zlschat.enums.UserStatus;
import br.com.gabriel.zlschat.services.UserService;
import br.com.gabriel.zlschat.services.UserStatusService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;

    @PostMapping("/register")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void register(@RequestBody RegisterCredentialsDTO registerCredentialsDTO) {
        this.userService.register(registerCredentialsDTO);
    }

    @MessageMapping("/update.status")
    public void updateStatus(@Payload UserStatus status, Principal principal) {
        this.userStatusService.setStatus(principal.getName(), status);
    }
}
