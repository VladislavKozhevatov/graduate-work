package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Комментарий к объявлению")

public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id пользователя")
    private Long pk;

    @Column(nullable = false)
    @Schema(description = "текст комментария")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @Schema(description = "Автор комментария")
    private UserEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_pk", nullable = false)
    @Schema(description = "id объявления")
    private AdEntity ad;

    @Column(nullable = false)
    @Schema(description = "Дата и время создания комментария")
    private Instant createdAt;
}