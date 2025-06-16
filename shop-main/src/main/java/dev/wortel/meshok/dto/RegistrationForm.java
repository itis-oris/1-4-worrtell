package dev.wortel.meshok.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistrationForm {

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Введите корректный email адрес")
    @Size(max = 255, message = "Email не должен превышать 255 символов")
    private String email;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, max = 16, message = "Пароль должен быть от 8 до 16 символов")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Пароль должен содержать: цифру, буквы в верхнем и нижнем регистре, спецсимвол"
    )
    private String password;

    @NotBlank(message = "Подтверждение пароля не может быть пустым")
    private String confirmPassword;
}