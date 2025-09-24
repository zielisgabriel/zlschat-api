package br.com.gabriel.zlschat.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.gabriel.zlschat.dtos.RegisterCredentialsDTO;
import br.com.gabriel.zlschat.enums.UserStatus;
import br.com.gabriel.zlschat.exceptions.UserNotFoundException;
import br.com.gabriel.zlschat.models.UserEntity;
import br.com.gabriel.zlschat.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserStatusService userStatusService;

    public void register(RegisterCredentialsDTO registerCredentialsDTO) {
        this.userRepository.findByEmail(registerCredentialsDTO.getEmail())
            .ifPresent(user -> {
                throw new RuntimeException("Email já está em uso");
            });
        this.userRepository.findByUsername(registerCredentialsDTO.getUsername())
            .ifPresent(user -> {
                throw new RuntimeException("Username já está em uso");
            });

        String passwordHashed = this.passwordEncoder.encode(registerCredentialsDTO.getPassword());
        registerCredentialsDTO.setPassword(passwordHashed);

        this.userRepository.save(registerCredentialsDTO.toUserEntity());
    }

    public Map<UserEntity, UserStatus> getFriends(String username) {
        UserEntity user = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(username));
        Map<UserEntity, UserStatus> friendsWithStatus = new HashMap<UserEntity, UserStatus>();
        List<String> friendsUsernames = user.getFriends();
        friendsUsernames.forEach(friendUsername -> {
            UserEntity friend = this.userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new UserNotFoundException("Amigo " + friendUsername + " não encontrado"));
            UserStatus userStatus = userStatusService.getStatus(friendUsername);
            friendsWithStatus.put(friend, userStatus);
        });
        return friendsWithStatus;
    }
}
