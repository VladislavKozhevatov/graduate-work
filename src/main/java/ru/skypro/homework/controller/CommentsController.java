package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Comments.CommentDTO;
import ru.skypro.homework.dto.Comments.CommentsDTO;
import ru.skypro.homework.dto.Comments.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.CommentService;

import java.util.Collections;
import java.util.List;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads/{adPk}/comments")
public class CommentsController {

    private final CommentService commentService;
    private final UserRepository userRepository;

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "404")
    @GetMapping
    public ResponseEntity<CommentsDTO> getComments(@PathVariable Long adPk) {
        return ResponseEntity.ok(commentService.getComments(adPk));
    }

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "404")
    @PostMapping
    public ResponseEntity<CommentDTO> addComment(
            @PathVariable Long adPk,
            @RequestBody CreateOrUpdateCommentDTO commentDTO,
            Authentication auth) {
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(commentService.addComment(adPk, commentDTO, user));
    }

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long adPk,
            @PathVariable Long commentId,
            Authentication auth) {
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        commentService.deleteComment(adPk, commentId, user);
        return ResponseEntity.ok().build();
    }

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "401")
    @ApiResponse(responseCode = "403")
    @ApiResponse(responseCode = "404")
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable Long adPk,
            @PathVariable Long commentId,
            @RequestBody CreateOrUpdateCommentDTO commentDTO,
            Authentication auth) {
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(commentService.updateComment(adPk, commentId, commentDTO, user));
    }
}