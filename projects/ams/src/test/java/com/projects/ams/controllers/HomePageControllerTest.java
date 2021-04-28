package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.sql.DataSource;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Chęci(true)
@DisplayName("User home page spec")
@WebMvcTest(HomePageController.class)
class HomePageControllerTest {

    @MockBean
    AdvertRepository advertRepository;

    @MockBean
    UserRepository userRepository;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DataSource dataSource;

    Advert someValidAdvert;

    @BeforeEach
    void setUp() {
        someValidAdvert = new Advert(1L, "advert", "advert", LocalDateTime.now(),
                new User(1L, "username", "password", "firstName", "lastName", true, new HashSet<>(), new HashSet<>()));
    }

    @Test
    @DisplayName("- on request from anonymous user should show home page with limited adverts")
    void getHomePage() throws Exception {
        Mockito.when(advertRepository.findFirst10ByOrderByPostedDesc())
                .thenReturn(List.of(someValidAdvert));

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI.create("/"))
                .with(SecurityMockMvcRequestPostProcessors.anonymous()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("home-page"))
                .andExpect(MockMvcResultMatchers.model().attribute("adverts", Matchers.iterableWithSize(1)));
    }

}