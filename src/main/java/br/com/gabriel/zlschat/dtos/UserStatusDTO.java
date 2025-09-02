package br.com.gabriel.zlschat.dtos;

import br.com.gabriel.zlschat.enums.UserStatus;
import lombok.Data;

@Data
public class UserStatusDTO {
    private String username;
    private UserStatus status;
}
