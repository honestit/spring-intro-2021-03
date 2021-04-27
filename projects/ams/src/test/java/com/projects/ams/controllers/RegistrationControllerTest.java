package com.projects.ams.controllers;

import com.projects.ams.model.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

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
    void prepareRegistrationPage() {

    }

}