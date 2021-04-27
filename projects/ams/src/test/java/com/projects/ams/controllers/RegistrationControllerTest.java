package com.projects.ams.controllers;

import com.projects.ams.model.repositories.UserRepository;
import com.projects.ams.requests.RegisterUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.DataSource;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User web registration spec")
// Włączamy mechanizm testów Spring Boot'a dla warstwy Web MVC
// ze wskazaniem klasy kontrolera, którego chcemy testować
@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    // Dostarczamy mocka dla zależności wymaganej w naszym kontrolerze
    @MockBean
    UserRepository userRepository;

    // Dostarczamy mocka dla zależności wymaganej w naszym kontrolerze
    @MockBean
    PasswordEncoder passwordEncoder;

    // Dostarczamy mocka dla zależności wymaganej przez konfigurację Spring Security (niestety).
    @MockBean
    DataSource dataSource;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("- on request should prepare registration page")
    void prepareRegistrationPage() throws Exception {
        // Metoda perform z MockMvc służy do wykonania żądania.
        // Żądanie budujemy z wykorzystaniem klasy MockMvcRequestBuilders
        mockMvc.perform(MockMvcRequestBuilders.get(URI.create("/register")))
                .andDo(MockMvcResultHandlers.print()) // do wyświetlenia podglądu odpowiedzi HTTP i stanu MVC
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("registration-form"))
                .andExpect(MockMvcResultMatchers.model().attribute("registration", new RegisterUserRequest()));
    }

    @Test
    @DisplayName("- on request should allow anonymous user")
    void requestFromAnonymousUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI.create("/register"))
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}