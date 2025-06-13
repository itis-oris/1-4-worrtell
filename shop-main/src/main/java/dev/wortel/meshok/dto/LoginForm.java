package dev.wortel.meshok.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LoginForm {
    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Некорректный email")
    private String email;
    @NotBlank(message = "Пароль не может быть пустым")
    //@Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

}