package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.register.NewPassword;
import ru.skypro.homework.dto.register.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.service.UserService;

import javax.validation.Valid;

@Tag(name = "Пользователи", description = "Раздел содержит методы по работе личной информацией пользователя")
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Обновляет пароль текущего пользователя.
     *
     * @param newPassword - объект с текущим и новым паролем
     */
    @Operation(summary = "Обновление пароля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    })
    @PostMapping("/set_password")
    public void updatePassword(@Valid @RequestBody NewPassword newPassword) {
        userService.updatePassword(newPassword);
    }

    /**
     * Получает информацию о текущем пользователе.
     *
     * @return информация о текущем пользователе
     */
    @Operation(summary = "Получение информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/me")
    public User getInfoAboutUser() {
        return userService.getInfoAboutUser();
    }

    /**
     * Обновляет информацию о текущем пользователе.
     *
     * @param newInfoUser - новые данные пользователя
     * @return обновленная информация о пользователе
     */
    @Operation(summary = "Обновление информации об авторизованном пользователе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PatchMapping("/me")
    public UpdateUser updateInfoAboutUser(@Valid @RequestBody UpdateUser newInfoUser) {
        return userService.updateInfoAboutUser(newInfoUser);
    }

    /**
     * Обновляет аватар текущего пользователя.
     *
     * @param image - файл нового аватара
     */
    @Operation(summary = "Обновление аватара авторизованного пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateAvatarUser(@RequestParam("image") MultipartFile image) {
        userService.updateAvatarUser(image);
    }

}


