package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Сервис для работы с изображениями.
 * Предоставляет методы для загрузки, сохранения и обработки изображений.
 */
public interface ImageService {

    /**
     * Сохраняет изображение и возвращает путь к файлу.
     *
     * @param file - файл изображения
     * @return путь к сохраненному файлу
     */
    String saveImage(MultipartFile file);

    /**
     * Получает изображение по пути к файлу.
     *
     * @param filePath - путь к файлу изображения
     * @return массив байтов изображения
     */
    byte[] getImage(String filePath);

    /**
     * Получает изображение объявления по ID.
     *
     * @param adId - ID объявления
     * @return массив байтов изображения
     */
    byte[] getAdImage(Integer adId);

    /**
     * Получает аватар пользователя.
     *
     * @return массив байтов изображения
     */
    byte[] getUserAvatar();

    /**
     * Удаляет изображение по пути к файлу.
     *
     * @param filePath - путь к файлу изображения
     */
    void deleteImage(String filePath);

    /**
     * Обновляет изображение.
     *
     * @param oldFilePath - старый путь к файлу
     * @param newFile     - новый файл изображения
     * @return новый путь к файлу
     */
    String updateImage(String oldFilePath, MultipartFile newFile);
}