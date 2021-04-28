package com.projects.ams.controllers;

import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.UserRepository;
import com.projects.ams.requests.RegisterUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraintvalidators.RegexpURLValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Slf4j
@Controller
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    /*
    Wstrzykiwania przez konstruktor używamy dla zależności, które są wymagane,
    a więc nasz kod bez nich nie może działać. Wstrzykiwania przez settery używamy
    dla zależności opcjonalnych oznaczając je jednocześnie jako nie wymagane (będzie null
    jeżeli nie ma kogo tam wstrzyknąć).
     */
//    @Autowired(required = false)
//    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @Autowired // Jeżeli w klasie jest tylko jeden konstruktor, to @Autowired nie jest obowiązkowe
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    /*
     * @GetMapping("/register") - można tak zrobić gdyby nie używać
     * @RequestMapping("/register") na klasie kontrolera
     */
    public String prepareRegistrationPage(Model model) {
        log.debug("Wywołanie strony rejestracji");
        model.addAttribute("registration", new RegisterUserRequest());
        return "registration-form";
        // Domyślna konfiguracja daje nam plik wynikowy:
        // templates/registration-form.html
    }

    @PostMapping
    /*
     * @PostMapping("/register") - można tak zrobić gdyby nie używać
     * @RequestMapping("/register") na klasie kontrolera
     */
    public String processRegistrationData(@Valid @ModelAttribute("registration") RegisterUserRequest request, Errors errors) {
        log.debug("Request data: {}", request);
        if (errors.hasErrors()) {
            return "registration-form";
        }
        User user = new User();
        user.setUsername(request.getUsername());
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.getRoles().add("ROLE_USER");
        user.setActive(true);

        log.debug("User to save: {}", user);

        // A jak zapisać do bazy? -> użyć Spring Data i repozytoriów :)
        // W repozytoriach Spring Data nie ma osobnych metod do utworzenia i do edycji,
        // jest jedna metoda: zapisz zmianę w repozytorium
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("User with same username already exists");
            errors.rejectValue("username", null, "User with given name already exists");
            return "registration-form";
        }
        userRepository.save(user);
        log.info("Saved user: {}", user);

        return "redirect:/"; // słowo redirect oznacza przekierowanie czyli wysłanie kolejnego żądania
        // na wskazany adres. Może być adres wewnątrz aplikacji, np: "/" albo "/login" albo adres
        // zewnętrzny, np: http://google.com itd
    }


}
