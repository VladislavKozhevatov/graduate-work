package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.service.ImageService;

import javax.servlet.http.HttpServletResponse;

@Tag(name = "Изображения", description = "Раздел содержит методы по работе с изображениями")
@Slf4j
@RequestMapping("/images")
@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Возвращает изображение
     */
    @Operation(summary = "Получение изображения")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(value = "/{imagePath}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable String imagePath, HttpServletResponse response) {
        byte[] image = imageService.getImage(imagePath);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(resolveImageType(imagePath));
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }

    private MediaType resolveImageType(String imagePath) {
        if (imagePath.endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else {
            return MediaType.IMAGE_JPEG;
        }
    }
}