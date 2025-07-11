package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * DTO для создания и обновления объявления
 */
@Data
public class CreateOrUpdateAd {

    @Schema(description = "заголовок объявления", minLength = 4, maxLength = 32)
    @Size(min = 4, max = 32, message = "Заголовок должен содержать от 4 до 32 символов")
    private String title;

    @Schema(description = "цена объявления", minimum = "0", maximum = "100000000")
    @Min(value = 0, message = "Цена не может быть отрицательной")
    @Max(value = 10000000, message = "Цена не может превышать 10 000 000")
    private Integer price;

    @Schema(description = "описание объявления", minLength = 8, maxLength = 64)
    @Size(min = 8, max = 64, message = "Описание должно содержать от 8 до 64 символов")
    private String description;
}
