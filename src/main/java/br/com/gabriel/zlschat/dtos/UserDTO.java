package br.com.gabriel.zlschat.dtos;

import br.com.gabriel.zlschat.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @Size(min = 3, max = 20, message = "O usuário deve ter entre 3 e 20 caracteres")
    private String username;
    @Email(message = "Email inválido")
    private String email;
    @Size(min = 8, max = 30, message = "A senha deve ter entre 8 e 30 caracteres")
    private String password;

    public UserEntity toEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(this.username);
        userEntity.setEmail(this.email);
        userEntity.setPassword(this.password);
        return userEntity;
    }
}
