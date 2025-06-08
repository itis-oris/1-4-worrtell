package dev.wortel.meshok.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RegistrationForm {

    private String email;
    private String password;
    private String confirmPassword;

}