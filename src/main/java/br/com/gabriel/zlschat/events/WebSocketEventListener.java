package br.com.gabriel.zlschat.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import br.com.gabriel.zlschat.dtos.UserStatusDTO;
import br.com.gabriel.zlschat.enums.UserStatus;
import br.com.gabriel.zlschat.services.UserService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {
    private final UserService userService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
        String username = (String) sessionDisconnectEvent.getUser().getName();
        UserStatusDTO userStatusDTO = new UserStatusDTO();
        userStatusDTO.setStatus(UserStatus.OFFLINE);
        userStatusDTO.setUsername(username);
        this.userService.updateStatus(userStatusDTO);
    }
}
