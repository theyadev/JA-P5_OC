package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class TestUserService {

    @Mock
    private UserRepository mockUserRepository;
    private UserService userServiceUnderTest;

    @BeforeEach
    void beforeEach() {
        userServiceUnderTest = new UserService(mockUserRepository);
    }

    @AfterEach
    void afterEach() {
        userServiceUnderTest = new UserService(mockUserRepository);
    }

    @Test
    void delete() {
        Long id = 1L;
        userServiceUnderTest.delete(id);
        assertThat(userServiceUnderTest.findById(id)).isNull();
    }

    @Test
    void findById() {
        Long id = 1L;
        when(mockUserRepository.findById(id)).thenReturn(Optional.of(User.builder().id(id).email("test@lastname.com")
                .lastName("Last Name").firstName("Test").password("test!1234").admin(false).build()));

        User result = userServiceUnderTest.findById(id);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getEmail()).isEqualTo("test@lastname.com");
        assertThat(result.getLastName()).isEqualTo("Last Name");
        assertThat(result.getFirstName()).isEqualTo("Test");
        assertThat(result.getPassword()).isEqualTo("test!1234");
        assertThat(result.isAdmin()).isFalse();
    }
}