package ru.skypro.homework.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Registration.Password;
import ru.skypro.homework.dto.User.UpdatedUser;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.Mapper.UserMapper;


import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder; // Подключение к сервису шифрования паролей
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageService imageService;

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    public void updatePassword(String email, Password passwordDto) {
        if (passwordDto == null || passwordDto.getCurrentPassword() == null || passwordDto.getNewPassword() == null) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        // Проверка текущего пароля
        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Неверный текущий пароль");
        }

        // Проверка что новый пароль отличается от старого
        if (passwordEncoder.matches(passwordDto.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Новый пароль должен отличаться от текущего");
        }

        log.info("Changing password for user: {}", email);
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    public UserDTO updateUser(String email, UpdatedUser updatedUser) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (updatedUser.getFirstName() != null) {
            user.setFirstName(updatedUser.getFirstName());
        }
        if (updatedUser.getLastName() != null) {
            user.setLastName(updatedUser.getLastName());
        }
        if (updatedUser.getPhone() != null) {
            user.setPhone(updatedUser.getPhone());
        }

        UserEntity savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser); // Преобразуем в DTO перед возвратом
    }


    public void updateUserImage(String email, MultipartFile image) throws IOException {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));

        // Удаляем старое изображение, если оно есть
        if (user.getImage() != null) {
            imageService.deleteImage(user.getImage());
        }

        // Сохраняем новое изображение
        String imagePath = imageService.saveUserImage(image);
        user.setImage(imagePath);

        userRepository.save(user);
    }

    public UserDTO getCurrentUser(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
        return userMapper.toDto(user);
    }
}