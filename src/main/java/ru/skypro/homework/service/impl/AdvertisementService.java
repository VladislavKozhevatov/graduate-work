package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.Exception.AdNotFoundException;
import ru.skypro.homework.Exception.ForbiddenException;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.AdImage;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdvertisementService {

    private final AdRepository adRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public List<AdEntity> getAllAds() {
        List<AdEntity> ads = adRepository.findAllByOrderByCreatedAtDesc();
        log.info("Found {} ads", ads.size());
        return ads;
    }

    @Transactional
    public AdEntity createAd(AdEntity ad, MultipartFile image, UserEntity author) throws IOException {
        ad.setAuthor(author);
        ad.setCreatedAt(LocalDateTime.now());

        AdImage adImage = createAdImage(image, ad);
        ad.setImage(adImage);

        AdEntity savedAd = adRepository.save(ad);
        log.info("Created new ad with id {}", savedAd.getPk());
        return savedAd;
    }

    @Transactional(readOnly = true)
    public AdEntity getAdById(Long id) {
        log.info("Getting ad by id {}", id);
        return adRepository.findById(id)
                .orElseThrow(() -> new AdNotFoundException("Ad not found with id: " + id));
    }

    @Transactional
    public void deleteAd(Long id, UserEntity user) throws IOException {
        log.info("Deleting ad with id {} by user {}", id, user.getId());
        AdEntity ad = getAdAndCheckAuthor(id, user);

        if (ad.getImage() != null) {
            imageService.deleteImage(ad.getImage().getFilePath());
        }

        adRepository.delete(ad);
        log.info("Deleted ad with id {}", id);
    }

    @Transactional
    public AdEntity updateAd(Long id, AdEntity updatedAd, UserEntity user) {
        log.info("Updating ad with id {} by user {}", id, user.getId());
        AdEntity existingAd = getAdAndCheckAuthor(id, user);

        existingAd.setTitle(updatedAd.getTitle());
        existingAd.setDescription(updatedAd.getDescription());
        existingAd.setPrice(updatedAd.getPrice());

        AdEntity savedAd = adRepository.save(existingAd);
        log.info("Updated ad with id {}", id);
        return savedAd;
    }

    @Transactional(readOnly = true)
    public List<AdEntity> getUserAds(UserEntity user) {
        log.info("Getting ads for user {}", user.getId());
        return adRepository.findAllByAuthor(user);
    }

    @Transactional
    public AdEntity updateAdImage(Long id, MultipartFile image, UserEntity user) throws IOException {
        log.info("Updating image for ad {} by user {}", id, user.getId());
        AdEntity ad = getAdAndCheckAuthor(id, user);

        // Delete old image if exists
        if (ad.getImage() != null) {
            imageService.deleteImage(ad.getImage().getFilePath());
        }

        // Create and save new image
        AdImage newImage = createAdImage(image, ad);
        ad.setImage(newImage);

        return adRepository.save(ad);
    }

    private AdImage createAdImage(MultipartFile image, AdEntity ad) throws IOException {
        AdImage adImage = new AdImage();
        String filename = imageService.saveImage(image);
        adImage.setFilePath(filename);
        adImage.setFileSize(image.getSize());
        adImage.setMediaType(image.getContentType());
        adImage.setPreview(image.getBytes());
        adImage.setAd(ad);
        return adImage;
    }

    private AdEntity getAdAndCheckAuthor(Long id, UserEntity user) {
        AdEntity ad = getAdById(id);

        if (!ad.getAuthor().getId().equals(user.getId())) {
            throw new ForbiddenException("User is not the author of the ad");
        }

        return ad;
    }
}
//
//    private final AdRepository adRepository;
//    private final UserRepository userRepository;
//    private final AdMapper adMapper;
//    private final AdImageRepository adImageRepository;
//
//    public AdsDTO getAllAds() {
//        List<AdEntity> ads = adRepository.findAll();
//        List<AdDTO> adDTOs = ads.stream()
//                .map(adMapper::toAdDTO)
//                .collect(Collectors.toList());
//        return new AdsDTO(ads.size(), adDTOs);
//    }
//
//    @Transactional
//    public AdDTO createAd(CreateOrUpdateAdDTO properties, MultipartFile image, String email) throws IOException {
//        UserEntity author = userRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//
//        AdImage adImage = processImage(image);
//
//        AdEntity ad = AdEntity.builder()
//                .title(properties.getTitle())
//                .description(properties.getDescription())
//                .price(properties.getPrice())
//                .author(author)
//                .adImage(adImage)
//                .build();
//
//        // Устанавливаем обратную связь
//        adImage.setAd(ad);
//
//        AdEntity savedAd = adRepository.save(ad);
//        return adMapper.toAdDTO(savedAd);
//    }
//
//    @Transactional(readOnly = true)
//    public ExtendedAdDTO getExtendedAd(Long id) {
//        AdEntity ad = adRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
//        return adMapper.toExtendedAd(ad);
//    }
//
//    @Transactional
//    public void deleteAd(Long id, String email) {
//        AdEntity ad = adRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
//
//        if (!ad.getAuthor().getEmail().equals(email)) {
//            throw new AccessDeniedException("Cannot delete other users' ads");
//        }
//
//        adRepository.delete(ad);
//    }
//
//    @Transactional(readOnly = true)
//    public AdsDTO getAdsMe(String email) {
//        List<AdDTO> ads = adRepository.findByAuthorEmail(email).stream()
//                .map(adMapper::toAdDTO)
//                .collect(Collectors.toList());
//        return new AdsDTO(ads.size(), ads);
//    }
//
//    @Transactional
//    public AdDTO updateAd(Long id, CreateOrUpdateAdDTO updateDto, String email) {
//        AdEntity ad = adRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
//
//        if (!ad.getAuthor().getEmail().equals(email)) {
//            throw new AccessDeniedException("Cannot edit other users' ads");
//        }
//
//        ad.setTitle(updateDto.getTitle());
//        ad.setDescription(updateDto.getDescription());
//        ad.setPrice(updateDto.getPrice());
//
//        return adMapper.toAdDTO(ad);
//    }
//
//    @Transactional
//    public String updateAdImage(Long adId, MultipartFile image, String username) throws IOException {
//        UserEntity currentUser = userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        AdEntity ad = adRepository.findById(adId)
//                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
//
//        if (!ad.getAuthor().getId().equals(currentUser.getId())) {
//            throw new AccessDeniedException("Cannot edit other users' ads");
//        }
//
//        AdImage newImage = processImage(image);
//        ad.updateImage(newImage);
//        adRepository.save(ad);
//
//        return "/ads/image/" + adId;
//    }
//
//    private AdImage processImage(MultipartFile image) throws IOException {
//        AdImage adImage = new AdImage();
//        adImage.setFilePath(image.getOriginalFilename());
//        adImage.setFileSize(image.getSize());
//        adImage.setMediaType(image.getContentType());
//        adImage.setPreview(image.getBytes());
//        return adImageRepository.save(adImage); // Сохраняем через репозиторий
//    }
//}