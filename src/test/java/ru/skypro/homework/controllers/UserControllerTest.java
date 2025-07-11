//package ru.skypro.homework.controllers;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//
//import ru.skypro.homework.dto.Role;
//import ru.skypro.homework.entity.UserEntity;
//import ru.skypro.homework.repository.UserRepository;
//
//import java.util.Base64;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@ActiveProfiles("test")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//class UserControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    private String authHeader;
//    private UserEntity testUser;
//
//    @BeforeAll
//    void init() {
//        userRepository.deleteAll();
//
//        testUser = userRepository.save(UserEntity.builder()
//                .email("testuser@example.com")
//                .password(passwordEncoder.encode("password"))
//                .firstName("Test")
//                .lastName("User")
//                .phone("+79990001122")
//                .role(Role.USER)
//                .build());
//
//        authHeader = "Basic " + Base64.getEncoder()
//                .encodeToString("testuser@example.com:password".getBytes());
//    }
//
//    @AfterEach
//    void cleanUp() {
//        userRepository.findByEmail("testuser@example.com").ifPresent(user -> {
//            user.setFirstName("Test");
//            user.setLastName("User");
//            user.setPhone("+79990001122");
//            userRepository.save(user);
//        });
//    }
//
//    @Test
//    void getCurrentUser_ShouldReturnUserInfo() throws Exception {
//        mockMvc.perform(get("/users/me")
//                        .header("Authorization", authHeader))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("testuser@example.com"))
//                .andExpect(jsonPath("$.firstName").value("Test"));
//    }
//
//    @Test
//    void updateUser_ShouldReturnUpdatedUser() throws Exception {
//        String updatedUserJson = """
//                {
//                    "firstName": "Updated",
//                    "lastName": "Name",
//                    "phone": "+79998887766"
//                }
//                """;
//
//        mockMvc.perform(patch("/users/me")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updatedUserJson)
//                        .header("Authorization", authHeader))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("Updated"));
//    }
//
//    @Test
//    void updatePassword_ShouldReturnOk() throws Exception {
//        String passwordJson = """
//                {
//                    "currentPassword": "password",
//                    "newPassword": "newPassword123"
//                }
//                """;
//
//        mockMvc.perform(post("/users/set_password")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(passwordJson)
//                        .header("Authorization", authHeader))
//                .andExpect(status().isOk());
//    }
//
//
//    @Test
//    void getCurrentUser_ShouldReturnUnauthorized_WhenNoAuth() throws Exception {
//        mockMvc.perform(get("/users/me"))
//                .andExpect(status().isUnauthorized());
//    }
//}