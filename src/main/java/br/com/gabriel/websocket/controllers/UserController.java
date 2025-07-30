package br.com.gabriel.websocket.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gabriel.websocket.dtos.UserDTO;
import br.com.gabriel.websocket.models.UserEntity;
import br.com.gabriel.websocket.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

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

    @PostMapping("/friends/request/{username}")
    public ResponseEntity<Void> requestFriendship(@PathVariable String username) {
        this.userService.requestFriendship(username);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/friends/accept/{username}")
    public ResponseEntity<Void> acceptFriendship(@PathVariable String username) {
        this.userService.acceptFriendship(username);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/friends/reject/{username}")
    public ResponseEntity<Void> rejectFriendship(@PathVariable String username) {
        this.userService.rejectFriendship(username);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
