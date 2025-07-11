package ru.skypro.homework.dto.Comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "Данные для создания или обновления комментария")
public class CreateOrUpdateCommentDTO {

    @NotBlank
    @Schema(description = "Текст комментария",
            minLength = 8,
            maxLength = 64)
    private String text = "";

}

