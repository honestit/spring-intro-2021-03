package com.projects.ams.rest;


import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

// Kontroler w stylu REST odwołuje się do zasobu, który obsługuje.
// W przeciwieństwie do konstrolera w stylu MVC, który odwołuje się do strony, którą obsługę; bez znaczenia, jakie zasoby na tej stronie są przetwarzane.

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

//  /api/users/{id}
    @GetMapping("/{id}")
    // Adnotacja @PathVariable powoduje, że w parametrze metody "id" znajdzie się wartość fragmentu ścieżki określona jako "{id}". Dla ścieżki "/api/users/2" będzie to wartość "2"
    public User getUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
    }
}
