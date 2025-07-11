package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.Exception.AdNotFoundException;
import ru.skypro.homework.Exception.CommentNotFoundException;
import ru.skypro.homework.Exception.ForbiddenException;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.dto.Comments.CommentDTO;
import ru.skypro.homework.dto.Comments.CommentsDTO;
import ru.skypro.homework.dto.Comments.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.*;

import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.service.Mapper.CommentMapper;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final CommentMapper commentMapper;

    public CommentsDTO getComments(Long adPk) {
        if (!adRepository.existsById(adPk)) {
            throw new AdNotFoundException("Объявление не найдено");
        }

        List<CommentDTO> comments = commentRepository.findAllByAd_Pk(adPk).stream()
                .map(commentMapper::toCommentDTO)
                .toList();

        return CommentsDTO.of(comments); // Чистый и понятный код
    }

    @Transactional
    public CommentDTO addComment(Long adPk, CreateOrUpdateCommentDTO commentDTO, UserEntity author) {
        AdEntity ad = adRepository.findById(adPk)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено"));

        CommentEntity comment = commentMapper.toEntity(commentDTO, author, ad);
        comment.setCreatedAt(Instant.now());

        CommentEntity savedComment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(savedComment);
    }

    @Transactional
    public void deleteComment(Long adPk, Long commentId, UserEntity user) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));

        validateCommentOwnership(comment, adPk, user);
        commentRepository.delete(comment);
    }

    @Transactional
    public CommentDTO updateComment(Long adPk, Long commentId,
                                    CreateOrUpdateCommentDTO commentDTO, UserEntity user) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Комментарий не найден"));

        validateCommentOwnership(comment, adPk, user);
        commentMapper.updateEntityFromDTO(commentDTO, comment);

        CommentEntity updatedComment = commentRepository.save(comment);
        return commentMapper.toCommentDTO(updatedComment);
    }

    private void validateCommentOwnership(CommentEntity comment, Long adPk, UserEntity user) {
        if (!comment.getAd().getPk().equals(adPk)) {
            throw new CommentNotFoundException("Комментарий не принадлежит указанному объявлению");
        }
        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("Вы не являетесь автором комментария");
        }
    }
}



//    @Transactional
//    public CommentDTO addComment(Long adId, String text, String username) {
//        UserEntity author = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//
//        AdEntity ad = adRepository.findById(adId)
//                .orElseThrow(() -> new EntityNotFoundException("Объявление не найдено"));
//
//        CommentEntity comment = CommentEntity.builder()
//                .text(text)
//                .author(author)
//                .ad(ad)
//                .createdAt(System.currentTimeMillis())
//                .build();
//
//        CommentEntity savedComment = commentRepository.save(comment);
//        return commentMapper.toDto(savedComment);
//    }
//}