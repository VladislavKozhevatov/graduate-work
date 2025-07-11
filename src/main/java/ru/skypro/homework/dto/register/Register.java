package ru.skypro.homework.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.skypro.homework.dto.Role;


import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// DTO для регистрации нового пользователя
@Data
public class Register {

    @Schema(description = "логин", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Логин должен содержать от 4 до 32 символов")
    private String username;

    @Schema(description = "пароль", minLength = 8, maxLength = 16)
    @Size(min = 8, max = 16, message = "Пароль должен содержать от 8 до 16 символов")
    private String password;

    @Schema(description = "имя пользователя", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "Имя должно содержать от 2 до 16 символов")
    private String firstName;

    @Schema(description = "фамилия пользователя", minLength = 2, maxLength = 16)
    @Size(min = 2, max = 16, message = "Фамилия должна содержать от 2 до 16 символов")
    private String lastName;

    @Schema(description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен соответствовать формату: +7 XXX XXX-XX-XX")
    private String phone;

    @Schema(description = "роль пользователя")
    private Role role;
}