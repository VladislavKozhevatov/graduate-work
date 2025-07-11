package ru.skypro.homework.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;
import ru.skypro.homework.service.ImageService;
import ru.skypro.homework.service.SecurityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final AdMapper adMapper;
    private final ImageService imageService;
    private final SecurityService securityService;

    public AdServiceImpl(AdRepository adRepository, AdMapper adMapper, ImageService imageService, SecurityService securityService) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
        this.securityService = securityService;
    }

    /**
     * Получение всех объявлений.
     *
     * @return возвращает список всех объявлений в виде ДТО Ads.
     */
    @Override
    public Ads getAllAds() {
        List<AdEntity> adEntityList = adRepository.findAll();
        List<Ad> adsList =
                adEntityList.stream()
                        .map(x -> adMapper.toAdDto(x))
                        .collect(Collectors.toList());
        Ads adsResult = new Ads();
        adsResult.setCount(adsList.size());
        adsResult.setResults(adsList);
        return adsResult;
    }

    /**
     * Добавление объявлений.
     *
     * @param newAd информация об объявлении.
     * @param image - картинка объявления.
     * @return - ДТО ad.
     */
    @Override
    public Ad addAd(CreateOrUpdateAd newAd, MultipartFile image) {
        UserEntity userEntity = securityService.getCurrentUser();
        AdEntity createAdEntity = adMapper.toAdEntity(newAd, userEntity);

        String imagePath = imageService.saveImage(image);
        createAdEntity.setImagePath(imagePath);
        adRepository.save(createAdEntity);
        return adMapper.toAdDto(createAdEntity);
    }

    /**
     * Получение информации об объявлении.
     * Сначала находим в БД объявление по его id, затем с помощью маппера сущность "Объявление"
     * переводим в нужную нам ДТО.
     *
     * @param adId - id объявления.
     * @return ДТО ExtendedAd.
     */
    @Override
    public ExtendedAd getInfoAboutAd(Integer adId) {

        Optional<AdEntity> ad = adRepository.findById(adId);
        if (ad.isPresent()) {
            return adMapper.toExtendedAdDto(ad.get());
        } else {
            throw new AdNotFoundException(adId);
        }
    }

    /**
     * Удаление объявления.
     *
     * @param adId - id объявления.
     */
    @Override
    public void deleteAd(Integer adId) {
        // Проверяем если ли объявление с таким номером в БД
        Optional<AdEntity> ad = adRepository.findById(adId);
        if (ad.isEmpty()) {
            throw new AdNotFoundException(adId);
        }

        // Проверяем есть ли права на удаление объявления
        securityService.checkPermissionToDeleteAd(ad.get());

        // Проверяем является текущей пользователь автором объявления или админом
        if (securityService.isOwnerOfAd(ad.get())) {
            adRepository.delete(ad.get());
        }
    }

    /**
     * Обновление информации об объявлении.
     *
     * @param adId     - id объявления.
     * @param updateAd - ДТО updateAd - новая информация по объявлению.
     * @return - ДТО ad.
     */
    @Override
    public Ad updateInfoAboutAd(Integer adId, CreateOrUpdateAd updateAd) {
        // Проверяем если ли объявление с таким номером в БД
        Optional<AdEntity> ad = adRepository.findById(adId);
        if (ad.isEmpty()) {
            throw new AdNotFoundException(adId);
        }

        // Проверяем права на редактирование объявления
        securityService.checkPermissionToEditAd(ad.get());

        adMapper.updateAdEntityFromDto(ad.get(), updateAd);
        adRepository.save(ad.get());
        return adMapper.toAdDto(ad.get());
    }

    /**
     * Получение объявлений авторизованного пользователя.
     *
     * @return ДТО Ads
     */
    @Override
    public Ads getAdsByUser() {

        // Получаем информацию об авторизованном пользователе
        UserEntity userEntity = securityService.getCurrentUser();

        List<AdEntity> adEntityList = adRepository.findByIdUser(userEntity.getId());

        List<Ad> adsList =
                new ArrayList<>();
        for (AdEntity x : adEntityList) {
            Ad adDto = adMapper.toAdDto(x);
            adsList.add(adDto);
        }
        Ads adsResult = new Ads();
        adsResult.setCount(adsList.size());
        adsResult.setResults(adsList);
        return adsResult;
    }

    /**
     * Обновление картинки объявления.
     * Сначала находим в БД объявление по его id. Затем проверяем права на редактирование.
     * Обновляем изображение, удаляя старое и сохраняя новое. Обновляем путь к картинке.
     *
     * @param adId  - id объявления.
     * @param image - новая картинка объявления.
     * @return возвращаем обновленный путь к новой картинке.
     */
    @Override
    public String updateAvatarAd(Integer adId, MultipartFile image) {
        // Проверяем если ли объявление с таким номером в БД
        Optional<AdEntity> ad = adRepository.findById(adId);
        if (ad.isEmpty()) {
            throw new AdNotFoundException(adId);
        }

        // Проверяем права на редактирование объявления
        securityService.checkPermissionToEditAd(ad.get());

        // Обновляем изображение, удаляя старое и сохраняя новое
        String oldImagePath = ad.get().getImagePath();
        String newImagePath = imageService.updateImage(oldImagePath, image);

        // Обновляем путь к картинке
        ad.get().setImagePath(newImagePath);

        adRepository.save(ad.get());
        return ad.get().getImagePath();
    }
}