package ru.skypro.homework.service;


import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;

public interface CommentService {

    Comments getComments(Integer idAd);

    Comment addComment(Integer idAd, CreateOrUpdateComment createComment);

    void deleteComment(Integer adId, Integer commentId);

    Comment updateComment(Integer adId, Integer commentId, CreateOrUpdateComment updateComment);

}