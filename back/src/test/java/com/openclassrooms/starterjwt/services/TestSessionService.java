package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TestSessionService {

    SessionService sessionService;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionRepository sessionRepository;

    List<Session> sessions;

    @BeforeEach
    void beforeEach() {
        sessionService = new SessionService(sessionRepository, userRepository);
        sessions = new ArrayList<>();
        sessions = List.of(
                Session.builder().id(1L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null)
                        .users(null).build(),
                Session.builder().id(2L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null)
                        .users(null).build(),
                Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).teacher(null)
                        .users(null).build());

    }

    @AfterEach
    void afterEach() {
        sessionService = null;
    }

    @Test
    void create() {
        Session session = Session.builder().id(4L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .teacher(null).users(null).build();
        when(sessionRepository.save(session)).thenReturn(session);
        Session sessionSaved = sessionService.create(session);
        assertThat(sessionSaved).isEqualTo(session);
    }

    @Test
    void delete() {
        sessionService.delete(1L);
        assertThat(sessionRepository.existsById(1L)).isFalse();
    }

    @Test
    void findAll() {
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> sessionsFound = sessionService.findAll();
        assertThat(sessionsFound).isEqualTo(sessions);
    }

    @Test
    void getById() {
        when(sessionRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(sessions.get(0)));
        Session sessionFound = sessionService.getById(1L);
        assertThat(sessionFound).isEqualTo(sessions.get(0));
    }

    @Test
    void update() {
        Session session = Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .teacher(null).users(null).build();
        when(sessionRepository.save(session)).thenReturn(session);
        Session sessionSaved = sessionService.update(3L, session);
        assertThat(sessionSaved).isEqualTo(session);
    }

    @Test
    void participate() {
        Session session = Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .teacher(new Teacher()).users(new ArrayList<User>()).build();
        when(sessionRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(session));
        User user = User.builder().id(1L).firstName("Test").lastName("Last Name").password("password").email("email")
                .build();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        sessionService.participate(3L, 1L);
        assertThat(session.getUsers().get(0)).isEqualTo(user);
    }

    @Test
    void noLongerParticipate() {
        Session session = Session.builder().id(3L).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now())
                .teacher(new Teacher()).users(new ArrayList<User>()).build();
        when(sessionRepository.findById(3L)).thenReturn(java.util.Optional.ofNullable(session));
        User user = User.builder().id(1L).firstName("Test").lastName("Last Name").password("password").email("email")
                .build();
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(user));
        sessionService.participate(3L, 1L);
        sessionService.noLongerParticipate(3L, 1L);
        assertThat(session.getUsers().size()).isEqualTo(0);
    }
}