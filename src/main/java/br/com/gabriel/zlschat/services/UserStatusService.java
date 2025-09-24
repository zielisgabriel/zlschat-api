package br.com.gabriel.zlschat.services;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import br.com.gabriel.zlschat.enums.UserStatus;
import br.com.gabriel.zlschat.exceptions.UserNotFoundException;
import br.com.gabriel.zlschat.models.UserEntity;
import br.com.gabriel.zlschat.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStatusService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    public static String USER_STATUS_KEY_PREFIX = "user:status:";

    public void setStatus(String username, UserStatus status) {
        UserEntity user = this.userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado"));
        List<String> friendsUsernames = user.getFriends();
        this.redisTemplate.opsForValue().set(USER_STATUS_KEY_PREFIX + username, status.getStatus());
        friendsUsernames.forEach(friend -> {
            this.simpMessagingTemplate.convertAndSendToUser(friend, "/topic/status", status);
        });
    }

    public UserStatus getStatus(String username) {
        String status = (String) this.redisTemplate.opsForValue().get(USER_STATUS_KEY_PREFIX + username);
        if (status == null) {
            return UserStatus.OFFLINE;
        }
        return UserStatus.valueOf(status);
    }
}
