package br.com.gabriel.zlschat.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import br.com.gabriel.zlschat.config.AuthenticationUserProvider;
import br.com.gabriel.zlschat.dtos.FriendDTO;
import br.com.gabriel.zlschat.dtos.UserDTO;
import br.com.gabriel.zlschat.dtos.UserStatusDTO;
import br.com.gabriel.zlschat.exceptions.UserAlreadyExistsException;
import br.com.gabriel.zlschat.exceptions.UserCannotBeAddedException;
import br.com.gabriel.zlschat.exceptions.UserNotFoundException;
import br.com.gabriel.zlschat.models.UserEntity;
import br.com.gabriel.zlschat.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private final ChatRoomService chatRoomService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationUserProvider authenticationUserProvider;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void register(UserDTO userDTO) {
        boolean isEmailExists = this.userRepository.existsByEmail(userDTO.getEmail());
        if (isEmailExists) {
            throw new UserAlreadyExistsException("Usuário já cadastrado");
        }
        
        String passwordHashed = passwordEncoder.encode(userDTO.getPassword());
        UserEntity user = userDTO.toEntity();
        user.setPassword(passwordHashed);

        this.userRepository.save(user);
    }

    public List<UserEntity> findByUsername(String username) {
        List<UserEntity> users = this.userRepository.findByUsernameStartingWith(username);
        List<UserEntity> usersWithoutMe = users.stream().filter(user -> !user.getUsername().equals(this.me().getUsername())).toList();
        return usersWithoutMe;
    }

    public UserEntity me() {
        String usernameOfAuthenticatedUser = this.authenticationUserProvider.getUsernameOfAuthenticatedUser();
        UserEntity user = this.userRepository.findByUsername(usernameOfAuthenticatedUser)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return user;
    }

    public void requestFriendship(String receiverUsername) {    
        UserEntity authenticatedUser = this.me();
        if (receiverUsername == authenticatedUser.getUsername() || authenticatedUser.getFriends().contains(receiverUsername)) {
            throw new RuntimeException("Usuário não pode ser adicionado");
        }
        UserEntity receiverUser = this.userRepository.findByUsername(receiverUsername)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        if (authenticatedUser.getFriendRequests().contains(receiverUsername)) {
            this.acceptFriendship(receiverUsername);
        }
        receiverUser.getFriendRequests().add(authenticatedUser.getUsername());
        this.userRepository.save(receiverUser);
    }

    public void acceptFriendship(String receiverUsername) {
        UserEntity authenticatedUser = this.me();
        if (receiverUsername == authenticatedUser.getUsername() || !authenticatedUser.getFriendRequests().contains(receiverUsername)) {
            throw new UserCannotBeAddedException("Usuário não pode ser adicionado ou não foi solicitado");
        }
        this.userRepository.findByUsername(receiverUsername).ifPresentOrElse(receiverUser -> {
            authenticatedUser.getFriends().add(receiverUsername);
            receiverUser.getFriends().add(authenticatedUser.getUsername());
            authenticatedUser.getFriendRequests().remove(receiverUsername);
            this.userRepository.save(authenticatedUser);
            this.userRepository.save(receiverUser);

            this.chatRoomService.createPrivateChatRoom(receiverUser.getUsername());
        }, () -> {
            authenticatedUser.getFriendRequests().remove(receiverUsername);
            throw new UserNotFoundException("Usuário nao encontrado");
        });
    }

    public void rejectFriendship(String username) {
        UserEntity authenticatedUser = this.me();
        if (username == authenticatedUser.getUsername() || !authenticatedUser.getFriendRequests().contains(username)) {
            throw new RuntimeException("Usuário não pode ser adicionado");
        }
        UserEntity userThatSendRequestFriend = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        authenticatedUser.getFriendRequests().remove(userThatSendRequestFriend.getUsername());
        this.userRepository.save(authenticatedUser);
    }

    public List<FriendDTO> getMyFriends(String username) {
        UserEntity user = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        return user.getFriends().stream().map(friendUsername -> {
            UserEntity friendEntity = this.userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
            FriendDTO friendDTO = new FriendDTO();
            friendDTO.setUsername(friendEntity.getUsername());
            friendDTO.setStatus(friendEntity.getStatus());
            return friendDTO;
        }).collect(Collectors.toList());
    }

    public void updateStatus(UserStatusDTO userStatusDTO) {
        List<FriendDTO> friendUsernames = this.getMyFriends(userStatusDTO.getUsername());
        for (FriendDTO friend : friendUsernames) {
            this.simpMessagingTemplate.convertAndSendToUser(friend.getUsername(), "/queue/user.status", userStatusDTO);
        }
        UserEntity userEntity = this.userRepository.findByUsername(userStatusDTO.getUsername())
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        userEntity.setStatus(userStatusDTO.getStatus());
        this.userRepository.save(userEntity);
        this.simpMessagingTemplate.convertAndSendToUser(userStatusDTO.getUsername(), "/queue/user.status", userStatusDTO);
    }
}
