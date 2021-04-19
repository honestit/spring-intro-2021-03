package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import com.projects.ams.requests.AddAdvertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class HomePageController {

    private AdvertRepository advertRepository;
    private UserRepository userRepository;

    public HomePageController(AdvertRepository advertRepository, UserRepository userRepository) {
        this.advertRepository = advertRepository;
        this.userRepository = userRepository;
    }

    // Metoda fabrykująca dostarczająca listę ogłoszeń dla każdej z metod kontrolera
    @ModelAttribute("adverts")
    public List<Advert> adverts() {
        List<Advert> adverts;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("What is our authentication? {}", authentication.getClass());
        if (authentication instanceof AnonymousAuthenticationToken) {
            adverts = advertRepository.findFirst10ByOrderByPostedDesc();
        } else {
            adverts = advertRepository.findAllByOrderByPostedDesc();
        }
        log.debug("Collected {} adverts for home page", adverts.size());
        return adverts;
    }

    @GetMapping
    public String prepareHomePage(Model model) {
        // Dodajemy na start pusty obiekt do zbindowania z formularzem
        model.addAttribute("advert", new AddAdvertRequest());
        return "home-page";
    }

    /*

    - opakować parametry w obiekt -> AddAdvertRequest
    - ustawimy w AddAdvertRequest pola takie jak parametry metody w kontrolerze (title, description)
    - użyjemy adnotacji @Valid w kontrolerze (w metodzie)
    - dodamy do parametrów metody Errors errors (do samodzielnej obsługi błędów walidacji)
    - zmienimy logikę działania metody, aby przy błędach nie zapisywała obiektu do bazy i zwracała
      widok z formularzem (czyli home-page.html)
    - możemy logusie dodać
    - dodamy obok adnotacji @Valid adnotację @ModelAttribute("advert"), aby obiekt z danymi żądania
      umieścić automatycznie w modelu dla widoku i aby dało się go powiązać z formularzem
    - zmodyfikujemy home-page.html, a dokładnie część z formularzem:
      - th:object
      - th:field
      - th:if (hasErrors)
      - th:each (errors)
      - th:text (error)

     */

    @PostMapping("/add-advert")
    public String saveAdvert(@Valid @ModelAttribute("advert") AddAdvertRequest addAdvertRequest, Errors errors) {
        log.debug("Adding new advert with title = '{}' and description = '{}'", addAdvertRequest.getTitle(), addAdvertRequest.getDescription());
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(fieldError -> log.debug("Error in request: {}", fieldError));
            return "home-page";
        }
        Advert newAdvert = new Advert();
        newAdvert.setTitle(addAdvertRequest.getTitle());
        newAdvert.setDescription(addAdvertRequest.getDescription());
        newAdvert.setPosted(LocalDateTime.now());
        // Pobieranie informacji o zalogowanym użytkowniku powiązanym z aktualnie przetwarzanym
        // żądaniem.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        newAdvert.setUser(user);
        advertRepository.save(newAdvert);
        log.info("Saved advert: {}", newAdvert);
        return "redirect:/";
    }
}
