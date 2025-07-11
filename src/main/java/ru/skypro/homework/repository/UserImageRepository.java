package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.skypro.homework.entity.UserImage;

import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    // Оптимизированный запрос только для данных изображения
    @Query("SELECT ui.data FROM UserImage ui WHERE ui.id = :id")
    Optional<byte[]> getImageDataById(@Param("id") Long id);
}

