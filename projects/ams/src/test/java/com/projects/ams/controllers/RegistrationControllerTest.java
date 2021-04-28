package com.projects.ams.controllers;

import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.UserRepository;
import com.projects.ams.requests.RegisterUserRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.net.URI;
import java.time.LocalDate;
import java.util.Set;

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

    @Test
    @DisplayName("- on request should save new user")
    void saveNewUser() throws Exception {
        Mockito.when(userRepository.existsByUsername("test")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("aB1@aB1#")).thenReturn("encoded");

        mockMvc.perform(MockMvcRequestBuilders
                .post(URI.create("/register"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "test")
                .param("firstName", "fName")
                .param("lastName", "lName")
                .param("password", "aB1@aB1#")
                .param("birthDate", LocalDate.now().minusYears(1).toString()))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        Mockito.verify(passwordEncoder, Mockito.times(1)).encode("aB1@aB1#");
        Mockito.verify(userRepository, Mockito.times(1)).existsByUsername("test");
//        Mockito.verify(userRepository, Mockito.times(1)).save(null); // Konkretna wartość, którą znamy
//        Mockito.verify(userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class)); // Jakaś wartość, która nas nie do końca interesuje i jej nie znamy

        // Kiedy interesuje nas co zostało przekazane do metody mocka, ale tego nie znamy
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class); // Tworzymy przechwytywacza
        Mockito.verify(userRepository, Mockito.times(1)).save(userCaptor.capture()); // Przechwutujemy parametr
        User savedUser = userCaptor.getValue();

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("test", savedUser.getUsername());
        Assertions.assertEquals("fName", savedUser.getFirstName());
        Assertions.assertEquals("lName", savedUser.getLastName());
        Assertions.assertEquals("encoded", savedUser.getPassword());
        Assertions.assertTrue(savedUser.getActive());
        Assertions.assertIterableEquals(Set.of("ROLE_USER"), savedUser.getRoles());
    }

    @ParameterizedTest
    @DisplayName("- on request when invalid data sent should return with errors in view")
    @CsvSource({
            "'','','','',''",
            "ab,ab,ab,ab,2999-01-01",
            "abcdefghijklm,ab,ab,abAbabAbabAb,2999-01-01"
    })
    void requestWithInvalidData(String username, String firstName, String lastName, String password, String birthDate) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post(URI.create("/register"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("firstName", firstName)
                .param("lastName", lastName)
                .param("password", password)
                .param("birthDate", birthDate))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("registration-form"))
                .andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors(
                        "registration",
                        "username", "firstName", "lastName", "password", "birthDate"
                ));

        Mockito.verifyNoInteractions(userRepository);
        Mockito.verify(userRepository, Mockito.never()).save(ArgumentMatchers.any());
    }

}