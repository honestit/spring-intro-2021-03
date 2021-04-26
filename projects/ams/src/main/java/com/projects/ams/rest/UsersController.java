package com.projects.ams.rest;


import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private PasswordEncoder passwordEncoder;

    public UsersController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    // Lista użytkowników (List<User>) zostanie automatycznie zserializowana do formatu JSON
    // FUTURE TIP: Zamiast zwracać listę encji User powinniśmy stworzyć klasę, np. UserView, w której znajdą się tylko te pola, które chcemy pokazać i listę Userów pobraną z repozytorium zamienić (przepisać dane, te które chcemy) na listę UserViewów
    public List<User> getUsers() {
        return userRepository.findAllWithDataBy();
    }

    // FUTURE TIP: Zamiast przyjmować encję User powinniśmy stworzyć klasę, np. UserRequest, w której znajdą się tylko te pola, które chcemy aby klient przesłał w celu stworzenia nowego użytkownika. Z obiektu UserRequest przepisujemy potem do encji User i zapisujemy encję User
    @PostMapping
    public void createUser(@RequestBody User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.getRoles().clear();
        user.getRoles().add("ROLE_USER");
        userRepository.save(user);
    }

//  GET /api/users/{id}
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

    // DELETE /api/users/{id}
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
