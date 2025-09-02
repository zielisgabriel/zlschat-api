package br.com.gabriel.zlschat.dtos;

import br.com.gabriel.zlschat.enums.UserStatus;
import lombok.Data;

@Data
public class FriendDTO {
    private String username;
    private UserStatus status;
}
