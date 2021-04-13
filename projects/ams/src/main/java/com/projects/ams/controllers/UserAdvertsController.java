package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class UserAdvertsController {
    private UserRepository userRepository;
    private AdvertRepository advertRepository;

    public UserAdvertsController(UserRepository userRepository, AdvertRepository advertRepository) {
        this.userRepository = userRepository;
        this.advertRepository = advertRepository;
    }

    @GetMapping("/user-adverts")
    public String getUserAdverts(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Advert> adverts = advertRepository.findAllByUserUsernameOrderByPostedDesc(username);
        model.addAttribute("adverts", adverts);
        return "user-adverts-page";
    }

    @GetMapping(path = "/user-adverts", params = {"username"})
    public String getUserAdverts(Model model, @RequestParam String username) {
        String loggedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUsername.equals(username)) {
            // /user-adverts?username=test -> /user-adverts
            return "redirect:/user-adverts";
        }
        List<Advert> adverts = advertRepository.findAllByUserUsernameOrderByPostedDesc(username);
        model.addAttribute("adverts", adverts);
        model.addAttribute("username", username);
        return "some-user-adverts-page";
    }

    /*
    Usuwanie ogłoszenia made by JAVA POL 64

    1. Metoda do usuwania ogłoszenia na podstawie id (dodać do repozytorium AdvertRepository)

      -> Lepsze rozwiązanie to pobrać po id ogłoszenie, zastanowić się co zrobić, jeżeli takie się nie znajdzie, a jak się znajdzie to usunąć (metoda `delete(Advert)` w repozytorium)

    2. Na stronie HTML (user-adverts-page.html) dodać link/guzik do usunięcia ogłoszenia
        - ścieżka linku: /delete-advert
        - potrzebujemy parametru żądania o nazwie "id" albo "advertId"
        - czyli ostateczna postać linku np. dla ogłoszenia o id = 10 będzie wyglądała tak:

          /delete-advert?advertId=10

    3. W kontrolerze UserAdvertsController dodajemy nową metodę do obsługi żądania usunięcia ogłoszenia
        - jaka adnotacja do mapowania? -> @PostMapping
        - jaka ścieżka? -> /delete-advert
        - jakie parametry żądania? -> advertId
        - jakie parametry metody? -> @RequestParam Long advertId
        - co zwracamy (jaki identyfikator widoku i czy jakikolwiek)? -> return "redirect:/user-adverts"
     */

    @PostMapping("/delete-advert")
    public String deleteAdvert(@RequestParam Long advertId) {
        log.debug("Removing advert with id = {}", advertId);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Advert advert = advertRepository.findByIdAndUserUsername(advertId, username);
        if (advert != null) {
            advertRepository.delete(advert);
            log.info("Removed advert: {}", advert);
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
//        very dangerous
//        advertRepository.deleteById(advertId);
        return "redirect:/user-adverts";
    }

}
