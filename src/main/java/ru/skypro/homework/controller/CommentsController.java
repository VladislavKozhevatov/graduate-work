package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments.CommentDTO;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Comments.CreateOrUpdateComment;

import java.util.Collections;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@Tag(name = "Комментарии")
@RequestMapping("/ads/{adId}/comments")
public class CommentsController {

    /**
     * Получение всех комментариев
     */
    @Operation(summary = "получение все комментариев")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = CommentDTO[].class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public List<CommentDTO> getComments(@PathVariable Long adId) {
        return Collections.emptyList();
    }

    /**
     * Получение комментария по id
     */
    @Operation(summary = "Добавление комментария")
    @PostMapping
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public CommentDTO addComment(@PathVariable Long adId,
                                 @RequestBody CreateOrUpdateComment comment) {
        return new CommentDTO();
    }

    /**
     * Удаление комментария по id
     */
    @Operation(summary = "Удаление комментария")
    @DeleteMapping("/{commentId}")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public ResponseEntity<?> deleteComment(@PathVariable Long adId,
                                           @PathVariable Long commentId) {
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление комментария по id
     */
    @Operation(summary = "Обновление комментария")
    @PatchMapping("/{commentId}")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(schema = @Schema(implementation = CommentDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public CommentDTO updateComment(@PathVariable Long adId,
                                    @PathVariable Long commentId,
                                    @RequestBody CreateOrUpdateComment comment) {
        return new CommentDTO();
    }
}
