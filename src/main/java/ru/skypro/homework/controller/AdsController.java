package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.skypro.homework.dto.Advertisement.AdDTO;
import ru.skypro.homework.dto.Advertisement.Ads;
import ru.skypro.homework.dto.Advertisement.CreateOrUpdateAd;
import ru.skypro.homework.dto.Advertisement.ExtendedAd;
import ru.skypro.homework.service.Mapper.AdMapper;
import ru.skypro.homework.service.impl.AdvertisementService;
import ru.skypro.homework.service.impl.ImageService;
import org.springframework.security.core.Authentication;

import java.io.IOException;

/**
 * Контроллер для работы с объявлениями
 *
 * @CrossOrigin(value = "http://localhost:3000") - разрешает доступ к API с любого домена
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления", description = "Операции с объявлениями")
public class AdsController {

    private final AdvertisementService advertisementService;
    private final ImageService imageService;
    private AdMapper adMapper;

    /**
     * Метод для получения всех объявлений
     */
    @Operation(summary = "Получение всех объявлений", tags = "Объявления")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Ads.class)))
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Ads getAllAds() {
        return advertisementService.getAllAds();
    }

    /**
     * Метод для добавления нового объявления
     *
     * @SecurityRequirement(name = "basicAuth") - обязательно требуется авторизация
     */
    @Operation(summary = "Добавление нового объявления")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "basicAuth")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = AdDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ResponseStatus(HttpStatus.CREATED)
    public AdDTO addAd(@RequestPart("properties") CreateOrUpdateAd properties,
                       @RequestPart("image") MultipartFile image, Authentication authentication) throws IOException {
        return advertisementService.createAd(properties, image, authentication.getName());
    }

    /**
     * Метод для получения информации об объявлении по id
     */
    @Operation(summary = "Получение информации об объявлении")
    @GetMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ExtendedAd.class)))
    public ExtendedAd getAd(@PathVariable Long id) {
        return advertisementService.getExtendedAd(id);
    }

    /**
     * Метод для удаления объявления
     */
    @Operation(summary = "Удаление объявления")
    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN') or @advertisementService.isAuthor(#id, authentication.name)")
    public void deleteAd(
            @PathVariable Long id,
            Authentication authentication) throws IOException {
        advertisementService.deleteAd(id, authentication.getName());
    }

    /**
     * Метод для обновления объявления
     */
    @Operation(summary = "Обновление объявления")
    @PatchMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = AdDTO.class)))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    public AdDTO updateAd(@PathVariable Long id,
                          @RequestBody CreateOrUpdateAd updatedAd,
                          Authentication authentication) {
        return advertisementService.updateAd(id, updatedAd, authentication.getName());
    }

    /**
     * Метод для получения объявлений текущего пользователя
     */
    @Operation(summary = "Получение объявлений текущего пользователя")
    @GetMapping("/me")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = Ads.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    public Ads getAdsMe(Authentication authentication) {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return advertisementService.getAdsMe(authentication.getName());
    }

    /**
     * Метод для обновления изображения объявления
     */
    @Operation(summary = "Обновление изображения объявления")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/octet-stream"))
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @SecurityRequirement(name = "basicAuth")
    public byte[] updateAdImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image,
            Authentication authentication) throws IOException {
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String imagePath = advertisementService.updateAdImage(id, image, authentication.getName());
        if (imagePath == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        }
        return imageService.loadImage(imagePath);
    }
}
