package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.ForbiddenException;
import ru.skypro.homework.Exception.UserNotFoundException;
import ru.skypro.homework.dto.Registration.PasswordDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;

import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.Mapper.UserMapper;


import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final PasswordEncoder passwordEncoder; // Подключение к сервису шифрования паролей
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    /**
     * Получение информации о текущем пользователе
     */
    public UserDTO getCurrentUser(Authentication authentication) {
        UserEntity user = getUserByEmail(authentication.getName());
        return userMapper.toDTO(user);
    }

    /**
     * Обновление информации о пользователе
     */
    public UserDTO updateUser(UpdateUserDTO updateDTO, Authentication authentication) {
        UserEntity user = getUserByEmail(authentication.getName());

        userMapper.updateEntityFromDTO(updateDTO, user);
        UserEntity updatedUser = userRepository.save(user);

        return userMapper.toDTO(updatedUser);
    }

    /**
     * Обновление пароля пользователя
     */
    @Transactional
    public void updatePassword(PasswordDTO passwordDTO, Authentication authentication) {
        UserEntity user = getUserByEmail(authentication.getName());

        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())) {
            throw new ForbiddenException("Текущий пароль неверен");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    /**
     * Обновление аватара пользователя
     */
    @Transactional
    public void updateUserImage(MultipartFile image, Authentication authentication) throws IOException {
        UserEntity user = getUserByEmail(authentication.getName());

        // Удаляем старое изображение, если оно есть
        if (user.getImage() != null) {
            imageService.deleteImage(user.getImage());
        }

        // Сохраняем новое изображение
        String filename = imageService.saveImage(image);
        user.setImage(filename);
        userRepository.save(user);
    }

    /**
     * Получение пользователя по email (вспомогательный метод)
     */
    private UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }
}










//    public UserDTO getCurrentUser(String email) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        return userMapper.toDTO(user);
//    }
//
//    public UserDTO updateUser(UpdateUserDTO updateDTO, String email) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        userMapper.updateEntityFromDTO(updateDTO, user);
//        UserEntity updatedUser = userRepository.save(user);
//        return userMapper.toDTO(updatedUser);
//    }
//
//    public void updateUserImage(MultipartFile image, String email) throws IOException {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        String oldImage = user.getImage();
//        user.setImage(imageService.saveImage(image));
//        userRepository.save(user);
//
//        if (oldImage != null) {
//            imageService.deleteImage(oldImage);
//        }
//    }
//
//
//



//    /**
//     * Метод для получения текущего пользователя
//     */
//    public UserDTO getCurrentUser(String email) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//        return userMapper.toDTO(user);
//    }
//
//    /**
//     * Метод для обновления пароля
//     */
//    public void updatePassword(String email, PasswordDTO passwordDto) {
//        if (passwordDto == null || passwordDto.getCurrentPassword() == null || passwordDto.getNewPassword() == null) {
//            throw new IllegalArgumentException("Пароль не может быть пустым");
//        }
//
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//
//        // Проверка текущего пароля
//        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Неверный текущий пароль");
//        }
//
//        // Проверка что новый пароль отличается от старого
//        if (passwordEncoder.matches(passwordDto.getNewPassword(), user.getPassword())) {
//            throw new IllegalArgumentException("Новый пароль должен отличаться от текущего");
//        }
//
//        log.info("Changing password for user: {}", email);
//        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
//        userRepository.save(user);
//    }
//
//    /**
//     * Метод для обновления данных пользователя
//     */
//    public UserDTO updateUser(String email, UpdateUserDTO updatedUser) {
//        UserEntity user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//
//        if (updatedUser.getFirstName() != null) {
//            user.setFirstName(updatedUser.getFirstName());
//        }
//        if (updatedUser.getLastName() != null) {
//            user.setLastName(updatedUser.getLastName());
//        }
//        if (updatedUser.getPhone() != null) {
//            user.setPhone(updatedUser.getPhone());
//        }
//
//        UserEntity savedUser = userRepository.save(user);
//        return userMapper.toDTO(savedUser); // Преобразуем в DTO перед возвратом
//    }

//    public void updateAvatar(Long userId, MultipartFile file) throws IOException {
//        UserEntity user = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
//
//        // Создаем новое изображение
//        UserImage newImage = new UserImage();
//        newImage.setData(file.getBytes());
//        newImage.setMediaType(file.getContentType());
//        newImage.setFileSize(file.getSize());
//        newImage.setUser(user); // Устанавливаем связь с пользователем
//
//        // Заменяем старое изображение новым
//        user.setImage(newImage);
//        userRepository.save(user);
//    }


