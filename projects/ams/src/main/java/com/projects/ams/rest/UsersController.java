package com.projects.ams.rest;

// Kontroler w stylu REST odwołuje się do zasobu, który obsługuje.
// W przeciwieństwie do konstrolera w stylu MVC, który odwołuje się do strony, którą obsługę; bez znaczenia, jakie zasoby na tej stronie są przetwarzane.

import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
// Używamy przedrostku "/api" dla ścieżek, aby je odróżnić od ścieżek używanych przez kontrolery MVC
@RequestMapping("/api/users")
public class UsersController {

    private UserRepository userRepository;

    public UsersController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    // Lista użytkowników (List<User>) zostanie automatycznie zserializowana do formatu JSON
    public List<User> getUsers() {
        return userRepository.findAllWithDataBy();
    }
}
