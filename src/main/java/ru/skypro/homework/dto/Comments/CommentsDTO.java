package ru.skypro.homework.dto.Comments;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Информация о комментариях")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDTO {
    @Schema(description = "общее количество комментариев")
    private Integer count;

    @Schema(description = "список комментариев")
    private List<CommentDTO> results;

    public static CommentsDTO of(List<CommentDTO> comments) {
        return new CommentsDTO(comments.size(), comments);
    }
}
