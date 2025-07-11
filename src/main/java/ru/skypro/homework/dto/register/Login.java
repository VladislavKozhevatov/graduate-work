package ru.skypro.homework.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

// DTO для входа в систему
@Data
public class Login {

    @Schema(description = "логин", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    private String username;

    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String password;
}