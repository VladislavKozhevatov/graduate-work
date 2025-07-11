package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с пользователями системы.
 * Предоставляет методы для поиска, сохранения и управления пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    /**
     * Поиск пользователя по email адресу.
     * Метод выполняет поиск в базе данных по полю email и возвращает
     * Optional с найденным пользователем или пустой Optional, если пользователь не найден.
     *
     * @param email - email адрес пользователя для поиска
     * @return Optional с объектом UserEntity, если пользователь найден, иначе пустой Optional
     * @see UserEntity
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Проверка существования пользователя по email адресу.
     * Метод проверяет, существует ли пользователь с указанным email в базе данных.
     *
     * @param email - email адрес пользователя для проверки
     * @return true если пользователь с указанным email существует, false иначе
     */
    boolean existsByEmail(String email);
}