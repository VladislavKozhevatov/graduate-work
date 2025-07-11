package ru.skypro.homework.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Table(name = "ads_images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size", nullable = false)
    private long fileSize;

    @Column(name = "media_type", nullable = false, length = 100)
    private String mediaType;

    @Column(name = "preview", nullable = false, columnDefinition = "bytea")
    private byte[] preview;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_pk", nullable = false)
    @JsonIgnore
    private AdEntity ad;
}