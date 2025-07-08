package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель объявления")
public class Advertisement {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор объявления")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @Schema(description = "Автор объявления")
    private UserEntity author;


    @Column(nullable = false)
    @Schema(description = "Заголовок объявления")
    private String title;

    @Column(nullable = false)
    @Schema(description = "Описание объявления")
    private String description;

    @Column(nullable = false)
    @Schema(description = "Цена объявления")
    private Integer price;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Schema(description = "Комментарии к объявлению")
    private List<Comment> comments;

    @Column(nullable = false)
    @NotNull
    @Schema(description = "Ссылка на изображение объявления")
    private String image;

}