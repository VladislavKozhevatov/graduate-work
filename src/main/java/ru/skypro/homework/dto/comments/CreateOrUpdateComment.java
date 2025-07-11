package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * DTO для создания и обновления комментария
 */
@Data
public class CreateOrUpdateComment {

    @Schema(description = "текст комментария", minLength = 8, maxLength = 64)
    @Size(min = 8, max = 64, message = "Текст комментария должен содержать от 8 до 64 символов")
    private String text;
}