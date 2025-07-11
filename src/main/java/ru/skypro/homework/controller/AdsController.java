package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Advertisement.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.Mapper.AdMapper;
import ru.skypro.homework.service.impl.AdvertisementService;
import ru.skypro.homework.service.impl.ImageService;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdvertisementService adService;
    private final ImageService imageService;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<AdsDTO> getAllAds() {
        List<AdEntity> ads = adService.getAllAds();
        AdsDTO response = mapToAdsDTO(ads);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdDTO> addAd(
            @RequestPart CreateOrUpdateAdDTO properties,
            @RequestPart MultipartFile image,
            Authentication auth) throws IOException {

        UserEntity user = getCurrentUser(auth);
        AdEntity newAd = adMapper.toEntity(properties, user);
        AdEntity createdAd = adService.createAd(newAd, image, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(adMapper.toAdDTO(createdAd));
    }

    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAdDTO> getAd(@PathVariable Long id) {
        AdEntity ad = adService.getAdById(id);
        return ResponseEntity.ok(adMapper.toExtendedAdDTO(ad));
    }

    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAd(
            @PathVariable Long id,
            Authentication auth) throws IOException {

        UserEntity user = getCurrentUser(auth);
        adService.deleteAd(id, user);
        return ResponseEntity.noContent().build();
    }

    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @PatchMapping("/{id}")
    public ResponseEntity<AdDTO> updateAd(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateAdDTO updatedAd,
            Authentication auth) {

        UserEntity user = getCurrentUser(auth);
        AdEntity adUpdates = adMapper.toEntity(updatedAd,user);
        AdEntity updatedEntity = adService.updateAd(id, adUpdates, user);

        return ResponseEntity.ok(adMapper.toAdDTO(updatedEntity));
    }

    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping("/me")
    public ResponseEntity<AdsDTO> getAdsMe(Authentication auth) {
        UserEntity user = getCurrentUser(auth);
        List<AdEntity> userAds = adService.getUserAds(user);
        AdsDTO response = mapToAdsDTO(userAds);
        return ResponseEntity.ok(response);
    }

    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdImage(
            @PathVariable Long id,
            @RequestParam MultipartFile image,
            Authentication auth) throws IOException {

        UserEntity user = getCurrentUser(auth);
        adService.updateAdImage(id, image, user);
        return ResponseEntity.ok().build();
    }

    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "404", description = "Not Found")
    @GetMapping(value = "/image/{filename}",
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAdImage(@PathVariable String filename) throws IOException {
        return ResponseEntity.ok(imageService.getImage(filename));
    }

    private UserEntity getCurrentUser(Authentication auth) {
        String username = auth.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    private AdsDTO mapToAdsDTO(List<AdEntity> ads) {
        return AdsDTO.builder()
                .count(ads.size())
                .results(ads.stream()
                        .map(adMapper::toAdDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}

//
//
//    private final AdvertisementService advertisementService;
//    private final ImageService imageService;
//
//    /**
//     * Получение всех объявлений
//     */
//    @Operation(summary = "Получение всех объявлений")
//    @GetMapping
//    @ApiResponse(responseCode = "200", description = "OK",
//            content = @Content(schema = @Schema(implementation = AdsDTO.class)))
//    @ApiResponse(responseCode = "500", description = "Internal Server Error")
//    public AdsDTO getAllAds() {
//        return advertisementService.getAllAds();
//    }
//
//    /**
//     *  Добавление нового объявления
//     */
//    @Operation(summary = "Добавление нового объявления")
//    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @SecurityRequirement(name = "basicAuth")
//    @ApiResponse(responseCode = "201", description = "Created",
//            content = @Content(schema = @Schema(implementation = AdDTO.class)))
//    @ApiResponse(responseCode = "400", description = "Bad Request")
//    @ApiResponse(responseCode = "401", description = "Unauthorized")
//    @ApiResponse(responseCode = "403", description = "Forbidden")
//    @ResponseStatus(HttpStatus.CREATED)
//    public AdDTO addAd(@RequestPart("properties") CreateOrUpdateAdDTO properties,
//                       @RequestPart("image") MultipartFile image,
//                       Authentication authentication) throws IOException {
//        return advertisementService.createAd(properties, image, authentication.getName());
//    }
//
//    /**
//     * Получение информации об объявлении по id
//     */
//    @Operation(summary = "Получение информации об объявлении")
//    @GetMapping("/{id}")
//    @ApiResponse(responseCode = "200", description = "OK",
//            content = @Content(schema = @Schema(implementation = ExtendedAdDTO.class)))
//    @ApiResponse(responseCode = "404", description = "Not Found")
//    public ExtendedAdDTO getAd(@PathVariable Long id) {
//        return advertisementService.getExtendedAd(id);
//    }
//
//    /**
//     * Обновление объявления
//     */
//    @Operation(summary = "Обновление объявления")
//    @PatchMapping("/{id}")
//    @ApiResponse(responseCode = "200", description = "OK",
//            content = @Content(schema = @Schema(implementation = AdDTO.class)))
//    @ApiResponse(responseCode = "400", description = "Bad Request")
//    @ApiResponse(responseCode = "401", description = "Unauthorized")
//    @ApiResponse(responseCode = "403", description = "Forbidden")
//    @ApiResponse(responseCode = "404", description = "Not Found")
//    public AdDTO updateAd(@PathVariable Long id,
//                          @RequestBody CreateOrUpdateAdDTO updatedAd,
//                          Authentication authentication) {
//        return advertisementService.updateAd(id, updatedAd, authentication.getName());
//    }
//
//    /**
//     * Удаление объявления
//     */
//    @Operation(summary = "Удаление объявления")
//    @DeleteMapping("/{id}")
//    @ApiResponse(responseCode = "204", description = "No Content")
//    @ApiResponse(responseCode = "401", description = "Unauthorized")
//    @ApiResponse(responseCode = "403", description = "Forbidden")
//    @ApiResponse(responseCode = "404", description = "Not Found")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasRole('ADMIN') or @advertisementService.isAuthor(#id, authentication.name)")
//    public void deleteAd(@PathVariable Long id,
//                         Authentication authentication) throws IOException {
//        advertisementService.deleteAd(id, authentication.getName());
//    }
//
//    /**
//     * Представление списка объявлений текущего пользователя
//     */
//    @Operation(summary = "Получение объявлений текущего пользователя")
//    @GetMapping("/me")
//    @ApiResponse(responseCode = "200", description = "OK",
//            content = @Content(schema = @Schema(implementation = AdsDTO.class)))
//    @ApiResponse(responseCode = "401", description = "Unauthorized")
//    public AdsDTO getAdsMe(Authentication authentication) {
//        if (authentication == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//        return advertisementService.getAdsMe(authentication.getName());
//    }
//
//    /**
//     * Обновление изображения объявления
//     */
//    @Operation(summary = "Обновление изображения объявления")
//    @PatchMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @SecurityRequirement(name = "basicAuth")
//    @ApiResponse(responseCode = "200", description = "OK",
//            content = @Content(schema = @Schema(implementation = String.class)))
//    @ApiResponse(responseCode = "400", description = "Bad Request")
//    @ApiResponse(responseCode = "401", description = "Unauthorized")
//    @ApiResponse(responseCode = "403", description = "Forbidden")
//    @ApiResponse(responseCode = "404", description = "Not Found")
//    public ResponseEntity<String> updateAdImage(@PathVariable Long id,
//                                                @RequestParam("image") MultipartFile image,
//                                                Authentication authentication) throws IOException {
//        if (authentication == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//        String imagePath = advertisementService.updateAdImage(id, image, authentication.getName());
//        return ResponseEntity.ok(imagePath);
//    }
//
//    @Operation(summary = "Получение изображения объявления")
//    @GetMapping(value = "/image/{id}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
//    @ApiResponse(responseCode = "200", description = "OK",
//            content = @Content(mediaType = "image/jpeg, image/png"))
//    @ApiResponse(responseCode = "404", description = "Not Found")
//    public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) throws IOException {
//        byte[] image = imageService.getImage("/ads/image/" + id);
//        return ResponseEntity.ok(image);
//    }
