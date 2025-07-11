package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Registration.PasswordDTO;
import ru.skypro.homework.dto.User.UpdateUserDTO;
import ru.skypro.homework.dto.User.UserDTO;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import ru.skypro.homework.service.impl.ImageService;
import ru.skypro.homework.service.impl.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
@Tag(name = "Пользователи", description = "Операции с пользователями")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ImageService imageService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<UserDTO> getCurrentUser(Authentication auth) {
        log.info("Запрос информации о текущем пользователе");
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(userService.getCurrentUser(auth));
    }

    @PatchMapping("/me")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    public ResponseEntity<UserDTO> updateUser(@RequestBody UpdateUserDTO updateDTO,
                                              Authentication auth) {
        log.info("Обновление информации пользователя");
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(userService.updateUser(updateDTO, auth));
    }

    @PostMapping("/set_password")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDTO.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public ResponseEntity<Void> updatePassword(@RequestBody PasswordDTO passwordDTO,
                                               Authentication auth) {
        log.info("Запрос на смену пароля");
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userService.updatePassword(passwordDTO, auth);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateUserImage(
            @RequestParam("image") MultipartFile imageFile,
            Authentication auth) throws IOException {
        log.info("Запрос на обновление аватара");
        String username = auth.getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        try (InputStream ignored = imageFile.getInputStream()) {
            userService.updateUserImage(imageFile, auth);
            return ResponseEntity.ok().build();
        }
    }


    @GetMapping(value = "/image/{filename}", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE
    })
    public ResponseEntity<byte[]> getUserImage(@PathVariable String filename) throws IOException {
        log.info("Запрос изображения пользователя: {}", filename);
        return ResponseEntity.ok(imageService.getImage(filename));
    }
}









//private final UserRepository userRepository;
//    /**
//     * Обновление пароля пользователя
//     */
//    @Operation(summary = "Обновление пароля пользователя")
//    @PostMapping("/set_password")
//    public ResponseEntity<Void> setPassword(
//            Principal principal,
//            @Valid @RequestBody PasswordDTO password) {
//        userService.updatePassword(principal.getName(), password);
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * Обновление пароля пользователя
//     */
//    @Operation(summary = "Получение данных текущего пользователя")
//    @GetMapping("/me")
//    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = UserDTO.class)))
//    @ApiResponse(responseCode = "401", description = "Unauthorized")
//    public ResponseEntity<UserDTO> getCurrentUser(Principal principal) {
//        return ResponseEntity.ok(userService.getCurrentUser(principal.getName()));
//    }
//
//    /**
//     * Обновление данных пользователя
//     */
//    @Operation(summary = "Обновление данных текущего пользователя")
//    @PatchMapping("/me")
//    public ResponseEntity<UserDTO> updateUser(
//            Principal principal, @RequestBody UpdateUserDTO updatedUser) {
//        return ResponseEntity.ok(userService.updateUser(principal.getName(), updatedUser));
//    }
//
//    /**
//     * Обновление аватара пользователя
//     */
//    @Operation(summary = "Обновление аватара авторизованного пользователя")
//    @PatchMapping(
//            value = "/me/image",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "OK"),
//            @ApiResponse(responseCode = "401", description = "Unauthorized")
//    })
//    public ResponseEntity<Void> updateUserImage(
//            Principal principal,
//            @RequestPart("image") MultipartFile image) throws IOException {
//
//        if (image.isEmpty()) {
//            throw new IllegalArgumentException("Image file cannot be empty");
//        }
//
//        userImageService.updateUserImage(principal.getName(), image);
//        return ResponseEntity.ok().build();
//    }
//}



