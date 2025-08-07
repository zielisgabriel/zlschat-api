package br.com.gabriel.zlschat.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginCredentialsDTO {
    @Email
    private String email;
    @Size(min = 8, max = 30, message = "A senha deve ter entre 8 e 30 caracteres")
    private String password;
}
