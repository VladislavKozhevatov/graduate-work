package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

/**
 * Репозиторий для работы с комментариями пользователя.
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    /**
     * Определение общего количества комментариев у объявления.
     *
     * @param adId - id объявления.
     * @return - кол-во комментариев.
     */
    @Query(value = "SELECT COUNT(*) FROM comments WHERE ads_id = :adId", nativeQuery = true)
    Integer getAmountCommentsByAdID(@Param("adId") Integer adId);

    /**
     * Получение всех комментариев у нужно объявления.
     *
     * @param adId - id объявления.
     * @return - список комментариев у объявления.
     */
    @Query(value = "SELECT * FROM comments WHERE ads_id = :adId", nativeQuery = true)
    List<CommentEntity> getListCommentEntityByAdID(@Param("adId") Integer adId);
}