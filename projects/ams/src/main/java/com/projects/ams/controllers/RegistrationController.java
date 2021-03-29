package com.projects.ams.controllers;

import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    /*
     * @GetMapping("/register") - można tak zrobić gdyby nie używać
     * @RequestMapping("/register") na klasie kontrolera
     */
    public String prepareRegistrationPage() {
        log.debug("Wywołanie strony rejestracji");
        return "registration-form";
        // Domyślna konfiguracja daje nam plik wynikowy:
        // templates/registration-form.html
    }

    @PostMapping
    /*
     * @PostMapping("/register") - można tak zrobić gdyby nie używać
     * @RequestMapping("/register") na klasie kontrolera
     */
    public String processRegistrationData(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName) {
        log.debug("username = {}, password = ***, firstName = {}, lastName = {}",
                username, firstName, lastName);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setActive(true);

        log.debug("User to save: {}", user);

        // A jak zapisać do bazy? -> użyć Spring Data i repozytoriów :)
        // W repozytoriach Spring Data nie ma osobnych metod do utworzenia i do edycji,
        // jest jedna metoda: zapisz zmianę w repozytorium
        if (userRepository.existsByUsername(username)) {
            log.warn("User with same username already exists");
            return "registration-form";
        }
        userRepository.save(user);
        log.info("Saved user: {}", user);

        return "redirect:/"; // słowo redirect oznacza przekierowanie czyli wysłanie kolejnego żądania
        // na wskazany adres. Może być adres wewnątrz aplikacji, np: "/" albo "/login" albo adres
        // zewnętrzny, np: http://google.com itd
    }


}
