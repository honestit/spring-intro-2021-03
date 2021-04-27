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

    @Test
    @DisplayName("- should throw an exception on null create user request")
    void convertFromNull() {
        // given
        CreateUserRequest nullRequest = null;

        // when + then
        assertThrows(IllegalArgumentException.class, () -> cut.from(nullRequest));

        // when + then + assertj
        Assertions.assertThatThrownBy(() -> cut.from(nullRequest))
                .hasMessage("Request cannot be null")
                .isInstanceOf(IllegalArgumentException.class);

        // wersja "po swojemu
//        Exception ex = null;
//        try {
//            cut.from(nullRequest);
//        } catch (Exception e) {
//            ex = e;
//        }
//        assertNotNull(ex);
//        assertEquals(IllegalArgumentException.class, ex.getClass());
    }
}