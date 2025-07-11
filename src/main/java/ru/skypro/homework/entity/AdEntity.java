package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "ads")
@Data
public class AdEntity {

    @Schema(description = "id объявления")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "заголовок объявления")
    @Column(name = "title")
    private String title;

    @Schema(description = "цена объявления")
    @Column(name = "price")
    private Integer price;

    @Schema(description = "описание объявления")
    @Column(name = "description")
    private String description;

    @Schema(description = "ссылка на картинку объявления")
    @Column(name = "image")
    private String imagePath;

    @Schema(description = "id автора объявления")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private UserEntity author;

    @OneToMany(mappedBy = "ad")
    private Set<CommentEntity> commentsInAd = new HashSet<>();

    public AdEntity() {
    }

    public AdEntity(String title, Integer price, String description, String imagePath, UserEntity author, Set<CommentEntity> commentsInAd) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.imagePath = imagePath;
        this.author = author;
        this.commentsInAd = commentsInAd;
    }
}