package br.com.gabriel.zlschat.events;

import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import br.com.gabriel.zlschat.services.UserStatusService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final RedisTemplate<String, Object> redisTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        String username = (String) sessionDisconnectEvent.getUser().getName();
        this.redisTemplate.delete(UserStatusService.USER_STATUS_KEY_PREFIX + username);
    }
}
