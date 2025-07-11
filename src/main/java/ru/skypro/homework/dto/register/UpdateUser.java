package ru.skypro.homework.dto.register;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * DTO для обновления данных пользователя
 */
@Data
public class UpdateUser {

    @Schema(description = "имя пользователя", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10, message = "Имя должно содержать от 3 до 10 символов")
    private String firstName;

    @Schema(description = "фамилия пользователя", minLength = 3, maxLength = 10)
    @Size(min = 3, max = 10, message = "Фамилия должна содержать от 3 до 10 символов")
    private String lastName;

    @Schema(description = "телефон пользователя", pattern = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}")
    @Pattern(regexp = "\\+7\\s?\\(?\\d{3}\\)?\\s?\\d{3}-?\\d{2}-?\\d{2}",
            message = "Телефон должен соответствовать формату: +7 XXX XXX-XX-XX")
    private String phone;
}