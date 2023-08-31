package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestUserController {
    @Mock
    UserMapper userMapper;

    @Mock
    UserService userService;

    @Mock
    SecurityContextHolder securityContext;

    User user;
    UserDto userDto;

    private UserController controller;

    @BeforeEach
    void beforeEach() {
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
        userDto = new UserDto(1L, "name@gmail.com", "Last Name", "First Name", true, "password", LocalDateTime.now(), LocalDateTime.now());
        controller = new UserController(userService, userMapper);
    }

    @Test
    @DisplayName("findById returns 200 when user is found")
    void findById() {
        when(this.userService.findById(1L)).thenReturn(user);
        when(this.userMapper.toDto(user)).thenReturn(userDto);

        ResponseEntity<?> response = controller.findById("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        UserDto responseBody = (UserDto) response.getBody();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getFirstName()).isEqualTo("First Name");
        assertThat(responseBody.getLastName()).isEqualTo("Last Name");
        assertThat(responseBody.getEmail()).isEqualTo("name@gmail.com");
        assertThat(responseBody.getPassword()).isEqualTo("password");
        assertThat(responseBody.isAdmin()).isTrue();
        assertThat(responseBody.getCreatedAt()).isNotNull();
        assertThat(responseBody.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("findById returns 404 when user is not found")
    void findByIdNotFound() {
        when(this.userService.findById(1L)).thenReturn(null);
        ResponseEntity<?> response = controller.findById("1");

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    @WithMockUser(username = "name@gmail.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 200 when user is found and coherent with the logged user")
    void delete() {
        when(this.userService.findById(user.getId())).thenReturn(user);
        // delete method is called save in the controller
        ResponseEntity<?> response = controller.save("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @WithMockUser(username = "test@test.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 401 when user is found but not coherent with the logged user")
    void deleteAnotherUser() {
        when(this.userService.findById(user.getId())).thenReturn(user);
        // delete method is called save in the controller
        ResponseEntity<?> response = controller.save("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    @WithMockUser(username = "name@gmail.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 404 when user is not found")
    void deleteNotFound() {
        when(this.userService.findById(user.getId())).thenReturn(null);

        // delete method is called save in the controller
        ResponseEntity<?> response = controller.save("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }


    @Test
    @WithMockUser(username = "name@gmail.com", password = "password", roles = "ADMIN")
    @DisplayName("Delete user will proceed with 400 when user id is not a number in string")
    void deleteError() {
        // delete method is called save in the controller
        ResponseEntity<?> response = controller.save("test");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }
}