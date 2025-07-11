package ru.skypro.homework.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;

import ru.skypro.homework.entity.UserEntity;

import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.SecurityService;

/**
 * Реализация сервиса для проверки прав доступа пользователей.
 * <p>
 * Этот сервис работает как "охранник" в нашем приложении:
 * - Разрешает или запрещает доступ к ресурсам
 * <p>
 * Например:
 * - Только владелец объявления может его редактировать
 * - Только владелец комментария может его удалить
 * - Администратор может делать все
 */
@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    public SecurityServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получает информацию о том, кто сейчас зашел в систему.
     *
     * @return UserEntity текущего пользователя
     * @throws AccessDeniedException если пользователь не аутентифицирован
     */
    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Пользователь не аутентифицирован");
        }

        // Находим пользователя в базе данных по email
        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new AccessDeniedException("Пользователь не найден"));
    }

    /**
     * Проверяет, является ли текущий пользователь администратором.
     *
     * @return true если пользователь является администратором
     */
    @Override
    public boolean isAdmin() {
        UserEntity currentUser = getCurrentUser();
        return Role.ADMIN.equals(currentUser.getRole());
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем объявления.
     *
     * @param ad - объявление для проверки
     * @return true если пользователь является владельцем объявления или администратором
     */
    @Override
    public boolean isOwnerOfAd(AdEntity ad) {
        if (ad == null) {
            return false;
        }

        UserEntity currentUser = getCurrentUser();
        // Проверяем: либо пользователь - администратор, либо он создал это объявление
        return isAdmin() || currentUser.getId().equals(ad.getAuthor().getId());
    }

    /**
     * Проверяет, является ли текущий пользователь владельцем комментария.
     *
     * @param comment - комментарий для проверки
     * @return true если пользователь является владельцем комментария или администратором
     */
    @Override
    public boolean isOwnerOfComment(CommentEntity comment) {
        if (comment == null) {
            return false;
        }

        UserEntity currentUser = getCurrentUser();
        // Проверяем: либо пользователь - администратор, либо он написал этот комментарий
        return isAdmin() || currentUser.getId().equals(comment.getAuthor().getId());
    }

    /**
     * Проверяет права на редактирование объявления.
     * Выбрасывает исключение, если у пользователя нет прав.
     *
     * @param ad - объявление для проверки
     * @throws AccessDeniedException если у пользователя нет прав на редактирование
     */
    @Override
    public void checkPermissionToEditAd(AdEntity ad) {
        if (!isOwnerOfAd(ad)) {
            throw new AccessDeniedException("Нет прав на редактирование объявления");
        }
    }

    /**
     * Проверяет права на удаление объявления.
     * <p>
     * Если у пользователя нет прав, выбрасывается исключение.
     *
     * @param ad - объявление для проверки
     * @throws AccessDeniedException если у пользователя нет прав на удаление
     */
    @Override
    public void checkPermissionToDeleteAd(AdEntity ad) {
        if (!isOwnerOfAd(ad)) {
            throw new AccessDeniedException("Нет прав на удаление объявления");
        }
    }

    /**
     * Проверяет права на редактирование комментария.
     * <p>
     * Если у пользователя нет прав, выбрасывается исключение.
     *
     * @param comment - комментарий для проверки
     * @throws AccessDeniedException если у пользователя нет прав на редактирование
     */
    @Override
    public void checkPermissionToEditComment(CommentEntity comment) {
        if (!isOwnerOfComment(comment)) {
            throw new AccessDeniedException("Нет прав на редактирование комментария");
        }
    }

    /**
     * Проверяет права на удаление комментария.
     * <p>
     * Если у пользователя нет прав, выбрасывается исключение.
     *
     * @param comment - комментарий для проверки
     * @throws AccessDeniedException если у пользователя нет прав на удаление
     */
    @Override
    public void checkPermissionToDeleteComment(CommentEntity comment) {
        if (!isOwnerOfComment(comment)) {
            throw new AccessDeniedException("Нет прав на удаление комментария");
        }
    }
}