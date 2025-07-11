package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;

import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.exception.AccessDeniedException;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.CommentNotFoundException;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.CommentService;
import ru.skypro.homework.service.SecurityService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;
    private final SecurityService securityService;

    public CommentServiceImpl(CommentRepository commentRepository, AdRepository adRepository, CommentMapper commentMapper, SecurityService securityService) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.commentMapper = commentMapper;
        this.securityService = securityService;
    }

    /**
     * Получение всех комментариев
     * относящихся к определенному объявлению
     *
     * @param idAd id объявления
     * @return DTO comments
     */
    @Override
    public Comments getComments(Integer idAd) {
        adRepository
                .findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));

        Comments comments = new Comments();
        comments.setCount(commentRepository.getAmountCommentsByAdID(idAd));
        if (comments.getCount() == 0) {
            comments.setResults(new ArrayList<>());
            return comments;
        }

        List<Comment> commentList = new ArrayList<>(comments.getCount());
        List<CommentEntity> entities = commentRepository.getListCommentEntityByAdID(idAd);
        entities.stream().map(commentMapper::toCommentDto).forEach(commentList::add);
        comments.setResults(commentList);
        return comments;
    }

    /**
     * Добавление нового комментария
     * к определенному объявлению
     *
     * @param idAd          id объявления
     * @param createComment DTO с текстом комментария
     * @return DTO comment
     */
    @Override
    public Comment addComment(Integer idAd, CreateOrUpdateComment createComment) {
        AdEntity adEntity = adRepository
                .findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        UserEntity userEntity = securityService.getCurrentUser();
        CommentEntity commentEntity = commentMapper.toCommentEntity(
                createComment,
                userEntity,
                adEntity
        );
        commentRepository.save(commentEntity);
        return commentMapper.toCommentDto(commentEntity);
    }

    /**
     * Удаление комментарий
     * к определенному объявлению
     *
     * @param idAd      id объявления
     * @param commentId id комментария
     */
    @Override
    public void deleteComment(Integer idAd, Integer commentId) {
        adRepository
                .findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        CommentEntity commentEntity = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!securityService.isOwnerOfComment(commentEntity)) {
            throw new AccessDeniedException();
        } else commentRepository.deleteById(commentId);
    }

    /**
     * Обновление комментария
     * к определенному объявлению
     *
     * @param idAd          id объявления
     * @param commentId     id комментария
     * @param updateComment DTO с новым текстом комментария
     * @return DTO comment
     */
    @Override
    public Comment updateComment(Integer idAd, Integer commentId, CreateOrUpdateComment updateComment) {
        adRepository
                .findById(idAd)
                .orElseThrow(() -> new AdNotFoundException(idAd));
        CommentEntity commentEntity = commentRepository
                .findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        if (!securityService.isOwnerOfComment(commentEntity)) {
            throw new AccessDeniedException();
        } else {
            commentMapper.updateCommentEntityFromDto(commentEntity, updateComment);
            commentRepository.save(commentEntity);
            return commentMapper.toCommentDto(commentEntity);
        }
    }
}