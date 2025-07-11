package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.ad.Ad;
import ru.skypro.homework.dto.ad.Ads;
import ru.skypro.homework.dto.ad.CreateOrUpdateAd;
import ru.skypro.homework.dto.ad.ExtendedAd;

public interface AdService {

    Ads getAllAds();

    Ad addAd(CreateOrUpdateAd newAd, MultipartFile image);

    ExtendedAd getInfoAboutAd(Integer adId);

    void deleteAd(Integer adId);

    Ad updateInfoAboutAd(Integer adId, CreateOrUpdateAd updateAd);

    Ads getAdsByUser();

    String updateAvatarAd(Integer adId, MultipartFile image);
}