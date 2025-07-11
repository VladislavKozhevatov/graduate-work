package ru.skypro.homework.dto.comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * DTO для списка комментариев
 */
@Data
public class Comments {

    @Schema(description = "общее количество комментариев")
    private Integer count;
    private List<Comment> results;
}