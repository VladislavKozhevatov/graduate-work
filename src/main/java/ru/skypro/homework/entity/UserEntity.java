package ru.skypro.homework.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users") // Изменено для соответствия миграции
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Модель пользователя")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Email пользователя")
    private String email;

    @Column(name = "first_name", nullable = false)
    @Schema(description = "Имя пользователя")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Schema(description = "Телефон пользователя")
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

    private String image; // Добавлено поле для хранения ссылки на аватар


}
