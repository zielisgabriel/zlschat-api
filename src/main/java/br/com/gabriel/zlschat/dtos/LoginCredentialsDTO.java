package br.com.gabriel.zlschat.dtos;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginCredentialsDTO {
    @Email
    private String email;
    private String password;
}
