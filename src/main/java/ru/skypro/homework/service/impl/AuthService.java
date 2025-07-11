package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.Exception.UserAlreadyExistsException;
import ru.skypro.homework.dto.Registration.LoginDTO;
import ru.skypro.homework.dto.Registration.PasswordDTO;
import ru.skypro.homework.dto.Registration.RegisterDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AuthServiceRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.Mapper.UserMapper;

import javax.persistence.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService implements AuthServiceRepository {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    public boolean login(String username, String password) {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return passwordEncoder.matches(password, user.getPassword()); // Проверяем пароль
    }

    public UserDTO register(RegisterDTO registerDTO) {
        if (userRepository.existsByEmail(registerDTO.getUsername())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        UserEntity user = userMapper.toEntity(registerDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toDTO(user);
    }

    @Override
    public void changePassword(String email, PasswordDTO passwordDTO) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }


    public UserEntity authenticate(LoginDTO loginDTO) {
        if (!login(loginDTO.getUsername(), loginDTO.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return userRepository.findByEmail(loginDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}





//    @Override
//    public boolean login(String userName, String password) {
//        log.info("Login attempt for user: {}" , userName);
//        UserEntity user = userRepository.findByEmail(userName)
//                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//
//        if (!passwordEncoder.matches(password, user.getPassword())) {
//            log.warn("Login failed - invalid password for user: {}", userName);
//            throw new BadCredentialsException("Неверный пароль");
//        }
//
//        log.info("Login successful for user: {}", userName);
//        return true;
//    }
//
//    @Override
//    public UserDTO register(RegisterDTO register) {
//        validateRegisterDto(register);
//
//        if (userRepository.existsByEmail(register.getUsername())) {
//            log.warn("Registration failed - user already exists: {}", register.getUsername());
//            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
//        }
//
//        UserEntity newUser = UserEntity.builder()
//                .email(register.getUsername())
//                .firstName(register.getFirstName())
//                .lastName(register.getLastName())
//                .phone(register.getPhone())
//                .password(passwordEncoder.encode(register.getPassword()))
//                .role(Role.USER) // По умолчанию назначаем роль USER
//                .build();
//
//        UserEntity savedUser = userRepository.save(newUser);
//        log.info("New user registered: {}", register.getUsername());
//
//        return userMapper.toDTO(savedUser);
//    }
//
//    @Override
//    public void changePassword(String email, PasswordDTO passwordDto) {
//        if (email == null || email.isBlank() || passwordDto == null) {
//            throw new IllegalArgumentException("Некорректные данные для смены пароля");
//        }
//
//        userService.updatePassword(email, passwordDto);
//    }
//
//    private void validateRegisterDto(RegisterDTO register) {
//        if (register == null) {
//            throw new IllegalArgumentException("Регистрационные данные не могут быть пустыми");
//        }
//
//        if (register.getUsername() == null || register.getUsername().isBlank()) {
//            throw new IllegalArgumentException("Email не может быть пустым");
//        }
//
//        if (register.getPassword() == null || register.getPassword().isBlank()) {
//            throw new IllegalArgumentException("Password не может быть пустым");
//        }
//
//        if (register.getFirstName() == null || register.getFirstName().isBlank()) {
//            throw new IllegalArgumentException("Имя не может быть пустым");
//        }
//
//        if (register.getLastName() == null || register.getLastName().isBlank()) {
//            throw new IllegalArgumentException("Фамилия не может быть пустой");
//        }
//    }
//}

