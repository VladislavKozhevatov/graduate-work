package ru.skypro.homework.service;

import org.springframework.security.access.AccessDeniedException;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;
import ru.skypro.homework.entity.UserEntity;

/**
 * Сервис для проверки прав доступа пользователей.
 * <p>
 * Этот сервис работает как "охранник" в нашем приложении:
 * - Проверяет, кто сейчас зашел в систему
 * - Проверяет, может ли пользователь выполнить то или иное действие
 * - Разрешает или запрещает доступ к ресурсам
 * <p>
 * Например:
 * - Только владелец объявления может его редактировать
 * - Только владелец комментария может его удалить
 * - Администратор может делать все
 */
public interface SecurityService {

    /**
     * Получает информацию о том, кто сейчас зашел в систему.
     *
     * @return UserEntity текущего пользователя
     * @throws AccessDeniedException если пользователь не аутентифицирован
     */
    UserEntity getCurrentUser();

    /**
     * Проверяет, является ли текущий пользователь администратором.
     *
     * @return true если пользователь является администратором
     */
    boolean isAdmin();

    /**
     * Проверяет, является ли текущий пользователь владельцем объявления.
     *
     * @param ad - объявление для проверки
     * @return true если пользователь является владельцем объявления или администратором
     */
    boolean isOwnerOfAd(AdEntity ad);

    /**
     * Проверяет, является ли текущий пользователь владельцем комментария.
     *
     * @param comment - комментарий для проверки
     * @return true если пользователь является владельцем комментария или администратором
     */
    boolean isOwnerOfComment(CommentEntity comment);

    /**
     * Проверяет права на редактирование объявления.
     * Выбрасывает исключение, если у пользователя нет прав.
     *
     * @param ad - объявление для проверки
     * @throws AccessDeniedException если у пользователя нет прав на редактирование
     */
    void checkPermissionToEditAd(AdEntity ad);

    /**
     * Проверяет права на удаление объявления.
     * <p>
     * Если у пользователя нет прав, выбрасывается исключение.
     *
     * @param ad - объявление для проверки
     * @throws AccessDeniedException если у пользователя нет прав на удаление
     */
    void checkPermissionToDeleteAd(AdEntity ad);

    /**
     * Проверяет права на редактирование комментария.
     * <p>
     * Если у пользователя нет прав, выбрасывается исключение.
     *
     * @param comment - комментарий для проверки
     * @throws AccessDeniedException если у пользователя нет прав на редактирование
     */
    void checkPermissionToEditComment(CommentEntity comment);

    /**
     * Проверяет права на удаление комментария.
     * <p>
     * Если у пользователя нет прав, выбрасывается исключение.
     *
     * @param comment - комментарий для проверки
     * @throws AccessDeniedException если у пользователя нет прав на удаление
     */
    void checkPermissionToDeleteComment(CommentEntity comment);
}