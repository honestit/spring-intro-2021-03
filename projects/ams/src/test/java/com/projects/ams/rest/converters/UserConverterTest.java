package com.projects.ams.rest.converters;

import com.projects.ams.model.domain.User;
import com.projects.ams.rest.request.CreateUserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Converting User class specification")
class UserConverterTest {

    UserConverter cut; // cut = class under test
    // +
    @BeforeEach
    void setUp() {
        cut = new UserConverter();
    }

    // Inaczej:
    // UserConvert cut = new UserConverter();


    @Test
    @DisplayName("- should convert create user request to user")
    void convert() {
        // given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("username");
        request.setPassword("password");
        request.setFirstName("firstName");
        request.setLastName("lastName");

        User expected = new User();
        expected.setUsername("username");
        expected.setPassword("password");
        expected.setFirstName("firstName");
        expected.setLastName("lastName");

        // when
        User result = cut.from(request);

        // then
        assertNotNull(result);
//        assertEquals(expected, result); // tego nie zrobimy, bo opiera się na metodzie equals z klasy User, która nam nie pasi
        assertEquals("username", result.getUsername());
        assertEquals("password", result.getPassword());
        assertEquals("firstName", result.getFirstName());
        assertEquals("lastName", result.getLastName());

        // Alternatywna wersja z assertj
        Assertions.assertThat(result)
                .isEqualToComparingOnlyGivenFields(expected, "username", "password", "firstName", "lastName");
    }

}