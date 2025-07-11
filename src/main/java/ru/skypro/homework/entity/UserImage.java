package ru.skypro.homework.entity;


import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "user_images")
@Data
public class UserImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "data", columnDefinition = "BYTEA")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] data;

    private Long fileSize;

    private String mediaType;

//    @JoinColumn(name = "user_id", nullable = false)
//    private UserEntity user;
}