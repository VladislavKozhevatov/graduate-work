package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.exception.AdNotFoundException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.UserEntity;

import ru.skypro.homework.exception.ImageNotFoundException;

import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.ImageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Реализация сервиса для работы с изображениями.
 * Сохраняет изображения в файловой системе.
 */
@Service
public class ImageServiceImpl implements ImageService {

    private static final String UPLOAD_DIR = "uploads/images/";
    private static final String DEFAULT_AVATAR_PATH = "/default-avatar.png";

    private final AdRepository adRepository;
    private final UserRepository userRepository;

    public ImageServiceImpl(AdRepository adRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    /**
     * Инициализация директории для загрузок после создания бина.
     */
    @PostConstruct
    public void initializeUploadDirectory() {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new ImageNotFoundException("Не удалось создать директорию для загрузок", e);
        }
    }

    /**
     * Сохраняет изображение в файловой системе.
     * Если файл пустой или null, возвращает путь к изображению по умолчанию.
     *
     * @param file - файл изображения для сохранения
     * @return путь к сохраненному изображению
     * @throws ImageNotFoundException если не удалось сохранить изображение
     */
    @Override
    public String saveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return DEFAULT_AVATAR_PATH;
        }

        try {
            String filename = generateUniqueFilename(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR, filename);
            Files.write(filePath, file.getBytes());

            return "/images/" + filename;
        } catch (IOException e) {
            throw new ImageNotFoundException("Не удалось сохранить изображение", e);
        }
    }

    /**
     * Получает изображение по пути к файлу.
     * Если путь указывает на изображение по умолчанию, возвращает его.
     *
     * @param filePath - путь к файлу изображения
     * @return массив байтов изображения
     * @throws ImageNotFoundException если изображение не найдено или не удалось прочитать
     */
    @Override
    public byte[] getImage(String filePath) {
        if (isDefaultImagePath(filePath)) {
            return getDefaultImage();
        }

        try {
            Path path = buildImagePath(filePath);
            if (!Files.exists(path)) {
                throw new ImageNotFoundException("Изображение не найдено: " + filePath);
            }

            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ImageNotFoundException("Не удалось прочитать изображение", e);
        }
    }

    /**
     * Получает изображение объявления по ID.
     *
     * @param adId - ID объявления
     * @return массив байтов изображения объявления
     * @throws AdNotFoundException    если объявление не найдено
     * @throws ImageNotFoundException если изображение не найдено или не удалось прочитать
     */
    @Override
    public byte[] getAdImage(Integer adId) {
        AdEntity ad = adRepository.findById(adId)
                .orElseThrow(() -> new AdNotFoundException("Объявление не найдено: " + adId));

        return getImage(ad.getImagePath());
    }

    /**
     * Получает аватар текущего пользователя.
     *
     * @return массив байтов аватара пользователя
     * @throws UserNotFoundException  если пользователь не найден
     * @throws ImageNotFoundException если изображение не найдено или не удалось прочитать
     */
    @Override
    public byte[] getUserAvatar() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден: " + username));

        return getImage(user.getImagePath());
    }

    /**
     * Удаляет изображение по пути к файлу.
     * Изображения по умолчанию не удаляются.
     *
     * @param filePath - путь к файлу изображения для удаления
     * @throws ImageNotFoundException если не удалось удалить изображение
     */
    @Override
    public void deleteImage(String filePath) {
        if (isDefaultImagePath(filePath)) {
            return; // Не удаляем изображение по умолчанию
        }

        try {
            Path path = buildImagePath(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new ImageNotFoundException("Не удалось удалить изображение", e);
        }
    }

    /**
     * Обновляет изображение, удаляя старое и сохраняя новое.
     *
     * @param oldFilePath - путь к старому файлу изображения
     * @param newFile     - новый файл изображения
     * @return путь к новому изображению
     * @throws ImageNotFoundException если не удалось сохранить или удалить изображение
     */
    @Override
    public String updateImage(String oldFilePath, MultipartFile newFile) {
        deleteImage(oldFilePath);
        return saveImage(newFile);
    }

    /**
     * Генерирует уникальное имя файла с сохранением расширения.
     */
    private String generateUniqueFilename(String originalFilename) {
        String extension = extractFileExtension(originalFilename);
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * Извлекает расширение файла из оригинального имени.
     */
    private String extractFileExtension(String originalFilename) {
        if (originalFilename != null && originalFilename.contains(".")) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return "";
    }

    /**
     * Проверяет, является ли путь путем к изображению по умолчанию.
     */
    private boolean isDefaultImagePath(String filePath) {
        return filePath == null || filePath.isEmpty() || DEFAULT_AVATAR_PATH.equals(filePath);
    }

    /**
     * Строит полный путь к файлу изображения.
     */
    private Path buildImagePath(String filePath) {
        String relativePath = filePath.replace("/images/", "");
        return Paths.get(UPLOAD_DIR, relativePath);
    }

    /**
     * Возвращает изображение по умолчанию.
     */
    private byte[] getDefaultImage() {
        // Здесь можно вернуть реальное изображение по умолчанию
        return new byte[0];
    }
}