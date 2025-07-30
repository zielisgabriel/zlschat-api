package br.com.gabriel.websocket.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gabriel.websocket.dtos.UserDTO;
import br.com.gabriel.websocket.exceptions.UserAlreadyExistsException;
import br.com.gabriel.websocket.exceptions.UserNotFoundException;
import br.com.gabriel.websocket.models.UserEntity;
import br.com.gabriel.websocket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(UserDTO userDTO) {
        boolean isEmailExists = this.userRepository.existsByEmail(userDTO.getEmail());
        if (isEmailExists) {
            throw new UserAlreadyExistsException("Usuário ja cadastrado");
        }
        
        String passwordHashed = passwordEncoder.encode(userDTO.getPassword());
        UserEntity user = userDTO.toEntity();
        user.setPassword(passwordHashed);

        this.userRepository.save(user);
    }

    public List<UserEntity> findByUsername(String username) {
        return this.userRepository.findByUsernameStartingWith(username);
    }

    public UserEntity me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameOfAuthenticatedUser = authentication.getPrincipal().toString();
        UserEntity user = this.userRepository.findByUsername(usernameOfAuthenticatedUser)
            .orElseThrow(() -> new UserNotFoundException("Usuário nao encontrado"));
        return user;
    }

    public void requestFriendship(String username) {
        UserEntity authenticatedUser = this.me();
        if (username == authenticatedUser.getUsername() || authenticatedUser.getFriends().contains(username)) {
            throw new RuntimeException("Usuário não pode ser adicionado");
        }
        UserEntity receiverUser = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Usuário nao encontrado"));
        receiverUser.getFriendRequests().add(authenticatedUser.getUsername());
        this.userRepository.save(receiverUser);
    }

    public void acceptFriendship(String username) {
        UserEntity authenticatedUser = this.me();
        if (username == authenticatedUser.getUsername() || !authenticatedUser.getFriendRequests().contains(username)) {
            throw new RuntimeException("Usuário não pode ser adicionado");
        }
        UserEntity receiverUser = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Usuário nao encontrado"));
        authenticatedUser.getFriends().add(username);
        receiverUser.getFriends().add(authenticatedUser.getUsername());
        authenticatedUser.getFriendRequests().remove(username);
        this.userRepository.save(authenticatedUser);
        this.userRepository.save(receiverUser);

        this.chatRoomService.createPrivateChatRoom(receiverUser.getUsername());
    }

    public void rejectFriendship(String username) {
        UserEntity authenticatedUser = this.me();
        if (username == authenticatedUser.getUsername() || !authenticatedUser.getFriendRequests().contains(username)) {
            throw new RuntimeException("Usuário nao pode ser adicionado");
        }
        UserEntity receiverUser = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Usuário nao encontrado"));
        receiverUser.getFriendRequests().remove(username);
        this.userRepository.save(receiverUser);
    }
}
