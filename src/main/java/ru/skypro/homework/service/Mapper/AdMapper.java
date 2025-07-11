package ru.skypro.homework.service.Mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.Advertisement.*;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.AdImage;
import ru.skypro.homework.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AdMapper {
    private final UserMapper userMapper;

    public AdEntity toEntity(CreateOrUpdateAdDTO adDTO, UserEntity author) {
        AdEntity entity = new AdEntity();
        entity.setTitle(adDTO.getTitle());
        entity.setPrice(adDTO.getPrice());
        entity.setDescription(adDTO.getDescription());
        entity.setAuthor(author);
        return entity;
    }

    public void updateEntityFromDTO(CreateOrUpdateAdDTO adDTO, AdEntity entity) {
        if (adDTO.getTitle() != null) {
            entity.setTitle(adDTO.getTitle());
        }
        if (adDTO.getPrice() != null) {
            entity.setPrice(adDTO.getPrice());
        }
        if (adDTO.getDescription() != null) {
            entity.setDescription(adDTO.getDescription());
        }
    }

    public AdDTO toAdDTO(AdEntity entity) {
        AdDTO dto = new AdDTO();
        dto.setPk(entity.getPk());
        dto.setAuthor(entity.getAuthor().getId());
        dto.setTitle(entity.getTitle());
        dto.setPrice(entity.getPrice());

        // Получаем путь к изображению из AdImage
        if (entity.getImage() != null) {
            dto.setImage("/ads/image/" + entity.getImage().getFilePath());
        }

        return dto;
    }

    public ExtendedAdDTO toExtendedAdDTO(AdEntity entity) {
        ExtendedAdDTO dto = new ExtendedAdDTO();
        dto.setPk(entity.getPk());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getEmail());

        // Получаем путь к изображению из AdImage
        if (entity.getImage() != null) {
            dto.setImage("/ads/image/" + entity.getImage().getFilePath());
        }

        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());
        return dto;
    }

    public AdsDTO mapToAdsDTO(List<AdEntity> ads) {
        return AdsDTO.builder()
                .count(ads.size())
                .results(ads.stream()
                        .map(this::toAdDTO)
                        .collect(Collectors.toList()))
                .build();
    }
}