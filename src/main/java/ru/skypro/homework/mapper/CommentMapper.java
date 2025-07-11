package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;

import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    /**
     * Преобразование сущности "комментарии" в DTO. Выполняется проверка на null.
     *
     * @param commentEntity - сущность "комментарий".
     * @return - ДТО "комментарий".
     */
    public Comment toCommentDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setPk(commentEntity.getId());
        comment.setAuthor(commentEntity.getAuthor().getId());
        comment.setAuthorFirstName(commentEntity.getAuthor().getFirstName());
        comment.setAuthorImage(commentEntity.getAuthor().getImagePath());
        comment.setCreatedAt(commentEntity.getCreatedAt());
        comment.setText(commentEntity.getText());
        return comment;
    }

    /**
     * Преобразование DTO в сущность при создании/обновления комментария. Выполняется проверка на null.
     *
     * @param createComment - ДТО "комментарий".
     * @param author        - автор комментария.
     * @param ad            - сущность "объявление".
     * @return сущность "комментарий".
     */
    public CommentEntity toCommentEntity(CreateOrUpdateComment createComment, UserEntity author, AdEntity ad) {
        if (createComment == null) {
            return null;
        }

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setText(createComment.getText());
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
        commentEntity.setCreatedAt(LocalDateTime.now());
        return commentEntity;
    }

    /**
     * Обновление существующей сущности "комментарии" данными из DTO. Выполняется проверка на null.
     *
     * @param commentEntity - сущность "комментарий".
     * @param updateComment - ДТО "комментарий".
     */
    public void updateCommentEntityFromDto(CommentEntity commentEntity, CreateOrUpdateComment updateComment) {
        if (commentEntity == null || updateComment == null) {
            return;
        }
        commentEntity.setText(updateComment.getText());
    }
}