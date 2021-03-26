package com.projects.ams.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegistrationController {

    @GetMapping
    public String prepareRegistrationPage() {
        log.debug("Wywołanie strony rejestracji");
        return "registration-form";
        // Domyślna konfiguracja daje nam plik wynikowy:
        // templates/registration-form.html
    }


}
