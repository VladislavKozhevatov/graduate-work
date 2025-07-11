package ru.skypro.homework.dto.ad;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для списка объявлений
 */
@Data
public class Ads {

    @Schema(description = "общее количество объявлений")
    private Integer count;
    private List<Ad> results;
}