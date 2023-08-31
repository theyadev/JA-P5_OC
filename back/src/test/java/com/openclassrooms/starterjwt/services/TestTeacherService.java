package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestTeacherService {

    @Mock
    TeacherRepository teacherRepository;
    TeacherService teacherServiceUnderTest;

    List<Teacher> teachers;

    @BeforeEach
    void beforeEach() {
        teacherServiceUnderTest = new TeacherService(teacherRepository);
        teachers = new ArrayList<>();
        teachers.add(Teacher.builder().id(1L).firstName("Test").lastName("Last Name").build());
        teachers.add(Teacher.builder().id(2L).firstName("Jean").lastName("Dupont").build());
        teachers.add(Teacher.builder().id(3L).firstName("Pierre").lastName("Martin").build());
    }

    @AfterEach
    void afterEach() {
        teachers.clear();
    }

    @Test
    void findAll() {
        when(teacherRepository.findAll()).thenReturn(teachers);
        List<Teacher> result = teacherServiceUnderTest.findAll();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0).getFirstName()).isEqualTo("Test");
        assertThat( result.get(0).getLastName()).isEqualTo("Last Name");
        assertThat(result.get(1).getFirstName()).isEqualTo("Jean");
        assertThat(result.get(1).getLastName()).isEqualTo("Dupont");
        assertEquals("Pierre", result.get(2).getFirstName());
        assertEquals("Martin", result.get(2).getLastName());
    }

    @Test
    void findById() {
        when(teacherRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(teachers.get(0)));
        Teacher result = teacherServiceUnderTest.findById(1L);
        assertEquals("Test", result.getFirstName());
        assertEquals("Last Name", result.getLastName());
        assertThat(result.getId()).isEqualTo(1L);
    }
}