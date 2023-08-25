package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionControllerTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;
    private SessionController sessionController;

    Teacher teacher;

    List<User> users;
    User user;

    List<Session> sessions;

    Session session;
    SessionDto sessionDto;

    @BeforeEach
    void beforeEach() {
        sessionController = new SessionController(sessionService, sessionMapper);
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .build();
        user = User.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .email("name@gmail.com")
                .password("password")
                .admin(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        users = List.of(user);
        session = Session.builder()
                .id(1L)
                .name("session")
                .teacher(teacher)
                .date(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)))
                .description("session description")
                .createdAt(null)
                .updatedAt(null)
                .users(users)
                .build();
        sessionDto = new SessionDto(1L, "session", Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), 1L,
                "session description", users.stream().map(User::getId).collect(Collectors.toList()), null, null);
    }

    @Test
    void findById() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        SessionDto sessionResponse = (SessionDto) response.getBody();
        assertThat(sessionResponse.getId()).isEqualTo(1L);
        assertThat(sessionResponse.getName()).isEqualTo("session");
        assertThat(sessionResponse.getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.getDate()).isNotNull();
    }

    @Test
    void findAll() {
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(List.of(sessionDto));

        ResponseEntity<?> response = sessionController.findAll();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        List<SessionDto> sessionResponse = (List<SessionDto>) response.getBody();
        assertThat(sessionResponse.get(0).getId()).isEqualTo(1L);
        assertThat(sessionResponse.get(0).getName()).isEqualTo("session");
        assertThat(sessionResponse.get(0).getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.get(0).getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.get(0).getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.get(0).getDate()).isNotNull();
    }

    @Test
    @Tag("Create")
    @DisplayName("Should return 200 when session is created")
    void create() {
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        SessionDto sessionResponse = (SessionDto) response.getBody();
        assertThat(sessionResponse.getId()).isEqualTo(1L);
        assertThat(sessionResponse.getName()).isEqualTo("session");
        assertThat(sessionResponse.getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.getDate()).isNotNull();
    }

    @Test
    @Tag("Update")
    @DisplayName("Should return 200 when session is updated")
    void update() {
        when(sessionService.update(session.getId(), session)).thenReturn(session);
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update(session.getId().toString(), sessionDto);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        SessionDto sessionResponse = (SessionDto) response.getBody();
        assertThat(sessionResponse.getId()).isEqualTo(1L);
        assertThat(sessionResponse.getName()).isEqualTo("session");
        assertThat(sessionResponse.getDescription()).isEqualTo("session description");
        assertThat(sessionResponse.getUsers().get(0)).isEqualTo(1L);
        assertThat(sessionResponse.getTeacher_id()).isEqualTo(1L);
        assertThat(sessionResponse.getDate()).isNotNull();
    }

    @Test
    @Tag("Update")
    @DisplayName("Should return 400 when session id is not a number")
    void updateWithStringId() {
        ResponseEntity<?> response = sessionController.update("test", sessionDto);
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Tag("Delete")
    @Test
    @DisplayName("Should return 200 when session is deleted")
    void delete() {
        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        when(sessionService.getById(session.getId())).thenReturn(session);

        ResponseEntity<?> response = sessionController.save(session.getId().toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Tag("Delete")
    @Test
    @DisplayName("Should return 400 when session id is not a number")
    void deleteWithStringId() {
        ResponseEntity<?> response = sessionController.save("test");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Tag("Delete")
    @Test
    @DisplayName("Should return 404 when session id is not found")
    void deleteNotFound() {
        when(sessionService.getById(session.getId())).thenReturn(null);
        ResponseEntity<?> response = sessionController.save(session.getId().toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Tag("Participate")
    @Test
    @DisplayName("Should return 200 when user participate to a session")
    void participate() {
        when(sessionService.getById(session.getId())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        ResponseEntity<?> response = sessionController.participate(session.getId().toString(), user.getId().toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Tag("Participate")
    @Test
    @DisplayName("Should return 400 when user id is not a number")
    void participateWithStringUserId() {
        ResponseEntity<?> response = sessionController.participate("test", user.getId().toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Tag("noLongerParticipate")
    @Test
    @DisplayName("Should return 200 when user no longer participate to a session")
    void noLongerParticipate() {
        when(sessionService.getById(session.getId())).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.noLongerParticipate(session.getId().toString(), user.getId().toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Tag("noLongerParticipate")
    @Test
    @DisplayName("Should return 400 when user id or session id is not a number")
    void noLongerParticipateWithStringIds() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("test", user.getId().toString());
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
}