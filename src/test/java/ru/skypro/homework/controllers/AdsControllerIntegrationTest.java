package ru.skypro.homework.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.entity.Advertisement;
import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.repository.AdvertisementRepository;
import ru.skypro.homework.repository.UserRepository;

import java.util.Base64;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AdsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AdvertisementRepository adRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserEntity testUser;
    private UserEntity adminUser;
    private String userAuthHeader;
    private String adminAuthHeader;
    private Advertisement testAd;

    @BeforeEach
    void setUp() {
        // Генерируем уникальные email для каждого теста
        String userEmail = "user_" + UUID.randomUUID() + "@example.com";
        String adminEmail = "admin_" + UUID.randomUUID() + "@example.com";

        // Создаем тестового пользователя
        testUser = userRepository.save(UserEntity.builder()
                .email(userEmail)
                .password(passwordEncoder.encode("password"))
                .firstName("User")
                .lastName("Test")
                .phone("+79990001122")
                .role(Role.USER)
                .build());

        // Создаем админа
        adminUser = userRepository.save(UserEntity.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode("admin"))
                .firstName("Admin")
                .lastName("Test")
                .phone("+79990003344")
                .role(Role.ADMIN)
                .build());

        userAuthHeader = "Basic " + Base64.getEncoder()
                .encodeToString((userEmail + ":password").getBytes());

        adminAuthHeader = "Basic " + Base64.getEncoder()
                .encodeToString((adminEmail + ":admin").getBytes());

        // Создаем тестовое объявление
        testAd = adRepository.save(Advertisement.builder()
                .title("Test Ad")
                .description("Test Description")
                .price(1000)
                .author(testUser)
                .image("/images/test.jpg") // Обязательное поле
                .build());
    }

    @AfterEach
    void tearDown() {
        adRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllAds_ShouldReturnAdsList() throws Exception {
        mockMvc.perform(get("/ads")
                        .header("Authorization", userAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].title").value("Test Ad"));
    }

    @Test
    void addAd_ShouldCreateNewAd() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", "test image".getBytes());

        String properties = """
                {
                    "title": "New Ad",
                    "description": "New Description",
                    "price": 2000
                }
                """;

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(new MockMultipartFile("properties", "",
                                "application/json", properties.getBytes()))
                        .header("Authorization", userAuthHeader))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Ad"));
    }

    @Test
    void getAd_ShouldReturnAdInfo() throws Exception {
        mockMvc.perform(get("/ads/{id}", testAd.getId())
                        .header("Authorization", userAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Ad"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void deleteAd_ShouldDeleteAd_WhenUserIsAuthor() throws Exception {
        mockMvc.perform(delete("/ads/{id}", testAd.getId())
                        .header("Authorization", userAuthHeader))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAd_ShouldDeleteAd_WhenUserIsAdmin() throws Exception {
        mockMvc.perform(delete("/ads/{id}", testAd.getId())
                        .header("Authorization", adminAuthHeader))
                .andExpect(status().isNoContent());
    }


    @Test
    void updateAd_ShouldUpdateAd() throws Exception {
        String updateJson = """
                {
                    "title": "Updated Title",
                    "description": "Updated Description",
                    "price": 1500
                }
                """;

        mockMvc.perform(patch("/ads/{id}", testAd.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson)
                        .header("Authorization", userAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    void getAdsMe_ShouldReturnUserAds() throws Exception {
        mockMvc.perform(get("/ads/me")
                        .header("Authorization", userAuthHeader))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(1))
                .andExpect(jsonPath("$.results[0].title").value("Test Ad"));
    }

    @Test
    void getAllAds_ShouldReturnUnauthorized_WhenNoAuth() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isUnauthorized());
    }
}
