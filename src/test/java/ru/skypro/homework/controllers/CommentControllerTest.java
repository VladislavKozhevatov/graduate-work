package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.skypro.homework.config.WebSecurityConfig;

import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.comments.Comment;
import ru.skypro.homework.dto.comments.Comments;
import ru.skypro.homework.dto.comments.CreateOrUpdateComment;
import ru.skypro.homework.entity.AdEntity;
import ru.skypro.homework.entity.CommentEntity;

import ru.skypro.homework.entity.UserEntity;
import ru.skypro.homework.service.CommentService;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CommentController.class)
@Import(WebSecurityConfig.class)
@MockBean(classes = {DataSource.class, JdbcUserDetailsManager.class})
@WithMockUser(username = "name@mail.ru", roles = {"USER"})
public class CommentControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    DataSource dataSource; // Мок DataSource

    @MockBean
    JdbcUserDetailsManager jdbcUserDetailsManager; // Мок JdbcUserDetailsManager

    @MockBean
    CommentService commentService;

    UserEntity testUserEntity;
    AdEntity testAdEntity;
    CommentEntity testCommentEntity;
    Comment testCommentDTO;
    Comments testCommentsDTO;
    CreateOrUpdateComment testCreateOrUpdateComment;

    @BeforeEach
    void setUp() {
        testUserEntity = new UserEntity();
        testUserEntity.setId(1);
        testUserEntity.setFirstName("Antony");
        testUserEntity.setLastName("Mackey");
        testUserEntity.setEmail("anmac@example.com");
        testUserEntity.setPassword("password");
        testUserEntity.setPhoneNumber("+9876543210");
        testUserEntity.setRole(Role.USER);
        testUserEntity.setImagePath("/avatar.jpg");

        testAdEntity = new AdEntity();
        testAdEntity.setId(1);
        testAdEntity.setAuthor(testUserEntity);
        testAdEntity.setTitle("Test Ad");
        testAdEntity.setDescription("Test Ad Description");
        testAdEntity.setImagePath("/avatar.jpg");
        testAdEntity.setPrice(10000);

        testCommentEntity = new CommentEntity();
        testCommentEntity.setId(1);
        testCommentEntity.setText("text text");
        testCommentEntity.setAuthor(testUserEntity);
        testCommentEntity.setCreatedAt(LocalDateTime.now());
        testCommentEntity.setAd(testAdEntity);

        testAdEntity.setCommentsInAd(Set.of(testCommentEntity));

        testCommentDTO = new Comment();
        testCommentDTO.setAuthor(testCommentEntity.getAuthor().getId());
        testCommentDTO.setText(testCommentEntity.getText());
        testCommentDTO.setAuthorImage(testCommentEntity.getAuthor().getImagePath());
        testCommentDTO.setPk(testCommentEntity.getId());
        testCommentDTO.setAuthorFirstName(testCommentEntity.getAuthor().getFirstName());
        testCommentDTO.setCreatedAt(testCommentEntity.getCreatedAt());

        List<Comment> list = List.of(testCommentDTO);
        testCommentsDTO = new Comments();
        testCommentsDTO.setCount(1);
        testCommentsDTO.setResults(list);
    }

    @Test
    @DisplayName("GET /ads/{id}/comments - получение всех комментариев объявления")
    void getCommentsTest() throws Exception {

        when(commentService.getComments(1)).thenReturn(testCommentsDTO);

        mvc.perform(MockMvcRequestBuilders
                        .get("/ads/1/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(testCommentsDTO.getCount()))
                .andExpect(content().json(objectMapper.writeValueAsString(testCommentsDTO)))
                .andDo(print());

        verify(commentService, times(1)).getComments(1);
    }

    @Test
    @DisplayName("POST /ads/{id}/comments - Добавление комментария к объявлению")
    void addCommentTest() throws Exception {

        when(commentService.addComment(
                1,
                testCreateOrUpdateComment))
                .thenReturn(testCommentDTO);

        mvc.perform(MockMvcRequestBuilders
                        .post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1))
                .addComment(
                        anyInt(),
                        any(CreateOrUpdateComment.class));
    }

    @Test
    @DisplayName("DELETE /ads/{adId}/comments/{commentId} - Удаление комментария к объявлению")
    void deleteCommentTest() throws Exception {

        doNothing().when(commentService).deleteComment(anyInt(), anyInt());

        mvc.perform(MockMvcRequestBuilders
                        .delete("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1)).deleteComment(anyInt(), anyInt());
    }

    @Test
    @DisplayName("PATCH /ads/{adId}/comments/{commentId} - Обновление комментария к объявлению")
    void updateCommentTest() throws Exception {

        when(commentService.updateComment(
                1,
                1,
                testCreateOrUpdateComment))
                .thenReturn(testCommentDTO);

        mvc.perform(MockMvcRequestBuilders
                        .patch("/ads/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCommentDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(commentService, times(1))
                .updateComment(
                        anyInt(),
                        anyInt(),
                        any(CreateOrUpdateComment.class));
    }

}