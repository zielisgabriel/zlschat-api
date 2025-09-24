package br.com.gabriel.zlschat.dtos;

import br.com.gabriel.zlschat.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterCredentialsDTO {
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,20}$", message = "Usuário deve ter entre 3 e 20 caracteres e pode conter letras, números, pontos, underline e hífens.")
    private String username;
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Senha deve ter no mínimo 8 caracteres, incluindo ao menos uma letra maiúscula, uma letra minúscula, um número e um caractere especial.")
    private String password;
    private String avatarUrl;

    public UserEntity toUserEntity() {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(this.username);
        userEntity.setEmail(this.email);
        userEntity.setPassword(this.password);
        userEntity.setAvatarUrl(this.avatarUrl);
        return userEntity;
    }
}
