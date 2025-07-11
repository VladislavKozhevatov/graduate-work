package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Registration.LoginDTO;
import ru.skypro.homework.dto.Registration.RegisterDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.repository.AuthServiceRepository;

import javax.validation.Valid;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация")
public class AuthController {

    private final AuthServiceRepository authService;

    @Operation(summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
            })
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@Valid @RequestBody LoginDTO login) {
        boolean isAuthenticated = authService.login(login.getUsername(), login.getPassword());
        if (!isAuthenticated) {
            throw new BadCredentialsException("Неверные учетные данные");
        }
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody RegisterDTO register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(register));
    }
}
