package ru.skypro.homework.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import ru.skypro.homework.config.TestSecurityConfig;
import ru.skypro.homework.controller.UserController;
import ru.skypro.homework.dto.register.NewPassword;
import ru.skypro.homework.dto.register.UpdateUser;
import ru.skypro.homework.dto.user.User;
import ru.skypro.homework.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfig.class)
@WithMockUser(username = "test@example.com", roles = {"USER"})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @Test
    @DisplayName("POST /users/set_password — Обновление пароля")
    public void whenUpdatePasswordTest() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("oldPassword123"); // минимум 8 символов
        newPassword.setNewPassword("newPassword123"); // минимум 8 символов

        doNothing().when(userService).updatePassword(any(NewPassword.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(userService).updatePassword(any(NewPassword.class));
    }

    @Test
    @DisplayName("POST /users/set_password — Обновление пароля с невалидными данными")
    public void whenUpdatePasswordWithInvalidDataTest() throws Exception {
        NewPassword newPassword = new NewPassword();
        newPassword.setCurrentPassword("old"); // слишком короткий пароль (минимум 8 символов)
        newPassword.setNewPassword("new"); // слишком короткий пароль (минимум 8 символов)

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPassword)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService, never()).updatePassword(any(NewPassword.class));
    }

    @Test
    @DisplayName("GET /users/me — Получение информации о пользователе")
    public void whenGetInfoAboutUserTest() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setPhone("+7 123 456-78-90");
        mockUser.setRole("USER");
        mockUser.setImage("/users/me/image");

        when(userService.getInfoAboutUser()).thenReturn(mockUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/me")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mockUser.getId()))
                .andExpect(jsonPath("$.email").value(mockUser.getEmail()))
                .andExpect(jsonPath("$.firstName").value(mockUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(mockUser.getLastName()))
                .andExpect(jsonPath("$.phone").value(mockUser.getPhone()))
                .andExpect(jsonPath("$.role").value(mockUser.getRole()))
                .andExpect(jsonPath("$.image").value(mockUser.getImage()))
                .andDo(print());

        verify(userService).getInfoAboutUser();
    }

    @Test
    @DisplayName("PATCH /users/me — Обновление информации о пользователе")
    public void whenUpdateInfoAboutUserTest() throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Jane");
        updateUser.setLastName("Smith");
        updateUser.setPhone("+7 987 654-32-10");

        when(userService.updateInfoAboutUser(any(UpdateUser.class))).thenReturn(updateUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(updateUser.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(updateUser.getLastName()))
                .andExpect(jsonPath("$.phone").value(updateUser.getPhone()))
                .andDo(print());

        verify(userService).updateInfoAboutUser(any(UpdateUser.class));
    }

    @Test
    @DisplayName("PATCH /users/me — Обновление информации с невалидными данными")
    public void whenUpdateInfoAboutUserWithInvalidDataTest() throws Exception {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Jo"); // слишком короткое имя (минимум 3 символа)
        updateUser.setLastName("Sm"); // слишком короткая фамилия (минимум 3 символа)
        updateUser.setPhone("123"); // неправильный формат телефона (должен быть +7 XXX XXX-XX-XX)

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUser)))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(userService, never()).updateInfoAboutUser(any(UpdateUser.class));
    }
}