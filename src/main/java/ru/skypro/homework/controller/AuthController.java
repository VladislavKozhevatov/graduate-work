package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Registration.Login;
import ru.skypro.homework.dto.Registration.Register;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.service.AuthService;



@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@Tag(name = "Авторизация")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешная авторизация"),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные"),
                    @ApiResponse(responseCode = "400", description = "Некорректные входные данные")
            })
    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody Login login) {
        return ResponseEntity.ok(authService.login(login.getUsername(), login.getPassword()));
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@Valid @RequestBody Register register) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(register));
    }
}
