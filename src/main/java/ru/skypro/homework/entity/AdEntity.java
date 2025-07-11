package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель объявления")
@Table(name="advertisement")
public class AdEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор объявления")
    @Column(name = "pk")
    private Long pk;

    @Column(nullable = false)
    @Schema(description = "Заголовок объявления")
    private String title;

    @Column(nullable = false)
    @Schema(description = "Цена объявления")
    private Integer price;

    @Column(nullable = false)
    @Schema(description = "Описание объявления")
    private String description;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @Schema(description = "Автор объявления")
    private UserEntity author;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private AdImage image;
    // private String image;

    private LocalDateTime createdAt;

}