package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestTeacherController {

    @Mock
    TeacherMapper teacherMapper;
    @Mock
    TeacherService teacherService;
    TeacherController controller;

    Teacher teacher;

    List<Teacher> teachers;

    List<TeacherDto> teacherDtos;

    @BeforeEach
    void beforeEach() {
        controller = new TeacherController(teacherService, teacherMapper);
        teachers = new ArrayList<>();
        teacher = Teacher.builder()
                .id(1L)
                .firstName("First Name")
                .lastName("Last Name")
                .build();
        teachers.add(teacher);
        teachers.add(new Teacher(2L, "Doe", "John", null, null));
        teachers.add(new Teacher(3L, "Joffrey", "Cina", null, null));
        teacherDtos = new ArrayList<>();
        teacherDtos.add(new TeacherDto(1L, "Last Name", "Test", null, null));
        teacherDtos.add(new TeacherDto(2L, "Doe", "John", null, null));
        teacherDtos.add(new TeacherDto(3L, "Cina", "Joffrey", null, null));
    }

    @Test
    @Tag("findAll")
    @DisplayName("findAll returns 200 when teachers are found")
    void findAll() {
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = controller.findAll();
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isInstanceOf(List.class);

        List<TeacherDto> body = (List<TeacherDto>) response.getBody();
        assertThat(body.size()).isEqualTo(3);
        assertThat(body.get(0).getFirstName()).isEqualTo("Test");
        assertThat(body.get(1).getFirstName()).isEqualTo("John");
        assertThat(body.get(2).getFirstName()).isEqualTo("Joffrey");
    }

    @Test
    @Tag("findById")
    @DisplayName("findById returns 200 when teacher is found")
    void findById() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDtos.get(0));

        ResponseEntity<?> response = controller.findById("1");
        assertThat(response.getStatusCodeValue()).isEqualTo(200);

        TeacherDto responseBody = (TeacherDto) response.getBody();
        assertThat(responseBody.getId()).isEqualTo(1L);
        assertThat(responseBody.getFirstName()).isEqualTo("Test");

    }

    @Test
    @Tag("findById")
    @DisplayName("findById returns 400 when number format is wrong")
    void findByIdWithStringId() {
        ResponseEntity<?> response = controller.findById("test");
        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    @Tag("findById")
    @DisplayName("findById returns 404 when teacher is not found")
    void findByIdNoFound() {
        when(teacherService.findById(5L)).thenReturn(null);

        ResponseEntity<?> response = controller.findById("5");
        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }
}