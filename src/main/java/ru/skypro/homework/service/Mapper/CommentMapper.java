package ru.skypro.homework.service.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Comments.CommentDTO;
import ru.skypro.homework.dto.Comments.CreateOrUpdateCommentDTO;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    public CommentEntity toEntity(CreateOrUpdateCommentDTO commentDTO, UserEntity author, AdEntity ad) {
        CommentEntity entity = new CommentEntity();
        entity.setText(commentDTO.getText());
        entity.setAuthor(author);
        entity.setAd(ad);
        return entity;
    }

    public void updateEntityFromDTO(CreateOrUpdateCommentDTO commentDTO, CommentEntity entity) {
        if (commentDTO.getText() != null) {
            entity.setText(commentDTO.getText());
        }
    }

    public CommentDTO toCommentDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setPk(entity.getPk());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(entity.getAuthor().getImage());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setText(entity.getText());
        return dto;
    }

//    public CommentsDTO toCommentsDTO(List<CommentEntity> entities) {
//        CommentsDTO dto = new CommentsDTO();
//        dto.setCount(entities.size());
//        dto.setResults(entities.stream()
//                .map(this::toCommentDTO)
//                .collect(Collectors.toList()));
//        return dto;
//    }
}
