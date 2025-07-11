package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
public class CommentEntity {

    @Schema(description = "id комментария")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "текст комментария")
    @Column(name = "text")
    private String text;

    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    @Column(name = "data_time")
    private LocalDateTime createdAt;

    @Schema(description = "объявление")
    @ManyToOne
    @JoinColumn(name = "ads_id")
    private AdEntity ad;

    @Schema(description = "id автора комментария")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity author;

    public CommentEntity() {
    }

    public CommentEntity(String text, LocalDateTime createdAt, AdEntity ad, UserEntity author) {
        this.text = text;
        this.createdAt = createdAt;
        this.ad = ad;
        this.author = author;
    }
}