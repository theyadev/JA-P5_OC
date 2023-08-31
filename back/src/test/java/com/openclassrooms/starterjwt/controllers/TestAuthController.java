package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MyAuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    String randomEmail = "name" + System.currentTimeMillis() + "@gmail.com";

    @Test
    void testUserAuthentication() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"yoga@studio.com\", \"password\": \"test!1234\" }")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUserAuthentication_Unauthorized() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"aaaa@studio.com\", \"password\": \"test!1234\" }")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        "{\"path\":\"\",\"error\":\"Unauthorized\",\"message\":\"Bad credentials\",\"status\":401}"));
    }

    @Test
    void testUserRegistration() throws Exception {
        

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"" + randomEmail + "\", \"password\": \"test!1234\", \"firstName\": \"First Name\", \"lastName\": \"Last Name\" }")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUserRegistrationDuplicateEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"email\": \"yoga@studio.com\", \"password\": \"test!1234\", \"firstName\": \"Test\", \"lastName\": \"Last Name\" }")
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"message\":\"Error: Email is already taken!\"}"));
    }
}
