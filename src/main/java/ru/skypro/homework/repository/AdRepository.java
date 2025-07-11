package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.AdEntity;

import java.util.List;

/**
 * Репозиторий для работы с объявлениями пользователя.
 */
@Repository
public interface AdRepository extends JpaRepository<AdEntity, Integer> {

    /**
     * Поиск всех объявлений, которые принадлежат определенному пользователю
     *
     * @param idUser - id пользователя.
     * @return - список всех объявлений пользователя.
     */
    @Query(value = "SELECT * FROM ads WHERE users_id = :idUser",
            nativeQuery = true)
    List<AdEntity> findByIdUser(@Param("idUser") Integer idUser);

}