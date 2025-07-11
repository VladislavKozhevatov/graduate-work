package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class ImageService {

    @Value("${image.upload.dir:uploads/images/}")
    private String uploadDirectory;

    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                log.info("Created upload directory: {}", uploadPath.toAbsolutePath());
            }
        } catch (IOException e) {
            log.error("Failed to create upload directory", e);
            throw new IllegalStateException("Could not initialize storage", e);
        }
    }

    @PreDestroy
    public void cleanup() {
        try {
            FileUtils.cleanDirectory(new File(uploadDirectory));
            log.info("Cleaned up upload directory");
        } catch (IOException e) {
            log.error("Failed to clean upload directory", e);
        }
    }

    public String saveImage(MultipartFile file) throws IOException {
        validateImageFile(file);

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = UUID.randomUUID() + fileExtension;
        Path destinationFile = Paths.get(uploadDirectory).resolve(uniqueFilename);

        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
        log.info("Saved image: {}", destinationFile);
        return uniqueFilename;
    }

    @Transactional(readOnly = true)
    public byte[] getImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            throw new IllegalArgumentException("Filename cannot be empty");
        }

        Path filePath = Paths.get(uploadDirectory).resolve(filename);
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File not found: " + filename);
        }

        return Files.readAllBytes(filePath);
    }

    public void deleteImage(String filename) throws IOException {
        if (filename == null || filename.isEmpty()) {
            return;
        }

        Path filePath = Paths.get(uploadDirectory).resolve(filename);
        if (Files.exists(filePath)) {
            Files.delete(filePath);
            log.info("Deleted image: {}", filePath);
        }
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
    }
}
