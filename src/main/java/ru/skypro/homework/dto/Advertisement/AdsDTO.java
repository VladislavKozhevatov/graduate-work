package ru.skypro.homework.dto.Advertisement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Список объявлений с пагинацией")
public class AdsDTO {

    @Schema(description = "Общее количество объявлений")
    private Integer count;

    @Schema(description = "Список объявлений")
    private List<AdDTO> results;
}
