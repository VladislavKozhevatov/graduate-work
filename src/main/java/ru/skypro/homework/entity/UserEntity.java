package ru.skypro.homework.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.skypro.homework.dto.Role;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
@Data
public class UserEntity implements UserDetails {

    @Schema(description = "id пользователя")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Schema(description = "имя пользователя")
    @Column(name = "first_name")
    private String firstName;

    @Schema(description = "фамилия пользователя")
    @Column(name = "last_name")
    private String lastName;

    @Schema(description = "логин пользователя")
    @Column(name = "email")
    private String email;

    @Schema(description = "пароль")
    @Column(name = "password")
    private String password;

    @Schema(description = "телефон пользователя")
    @Column(name = "phone")
    private String phoneNumber;

    @Schema(description = "роль пользователя")
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Schema(description = "ссылка на аватар пользователя")
    @Column(name = "image")
    private String imagePath;

    @Schema(description = "признак активного аккаунта")
    @Column(name = "enabled")
    private boolean enabled;

    @OneToMany(mappedBy = "author")
    private Set<CommentEntity> commentsByUser = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<AdEntity> adsByUser = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String firstName, String lastName, String email, String password, String phoneNumber, Role role, String imagePath, Set<CommentEntity> commentsByUser, Set<AdEntity> adsByUser) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.imagePath = imagePath;
        this.commentsByUser = commentsByUser;
        this.adsByUser = adsByUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }
}