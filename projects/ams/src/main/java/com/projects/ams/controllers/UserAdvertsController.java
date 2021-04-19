package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import com.projects.ams.requests.EditAdvertRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.awt.*;
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

    /*

    Edycja ogłoszenia made by JAVA POL 64 ???

    1. Dodać guzik (button w formularzu) na liście ogłoszeń użytkownika
       - musimy przesłać id ogłoszenia, które edytujemy -> input o typie hidden, nazwie "advertId" i wartości równej id ogłoszenia

    2. Nowa strona z formularzem do edycji ogłoszenia (edit-user-advert-page.html)
       - jaką metodą wysyłamy formularz? -> post
       - jaka akcja w formularzu? -> th:action="@{/edit-advert}"
       - jakie pola? ->
            - input typu hidden z nazwą "advertId",
            - input dla wprowadzenia title (użyć atrybuty value, a dokładnie to th:value="${advert.title}")
            - textarea dla wprowadzenia description (j.w.)
            - guzik do edycji (o typie "submit")
       - link do powrotu do listy ogłoszeń (w CSSach zrobimy, aby był piękny jak guzik)

    3. Potrzebujemy obsługi żądania wyświetlenia strony z formularzem do edycji
       - jaka ścieżka? -> "/edit-advert"
       - jakie mapowanie? -> @GetMapping
       - co zwracamy? -> return "edit-user-advert-page"
       - jakie parametry żądania? -> advertId
       - jakie parametry metody? -> @RequestParam Long advertId, Model model
       - w jaki sposób przygotujemy dane dla widoku? ->
          - pobieramy ogłoszenie na podstawie advertId + username (zalogowany użytkownik)
          - wstawiamy atrybut do modelu reprezentujący ogłoszenie do edycji (które przed chwilą pobraliśmy z repozytorium)

    4. Potrzebujemy obsługi żądania edycji ogłoszenia w kontrolerze UserAdvertsController
       - jaka ścieżka? -> "/edit-advert"
       - jakie mapowanie? -> @PostMapping
       - co zwracamy? -> return "redirect:/user-adverts"
       - jak zedytujemy? ->
            - pobieramy ogłoszenie na podstawie advertId + username (zalogowany użytkownik)
            - zamieniamy tytuł i opis na nowe
            - zapis w repozytorium (advertRepository.save)
       - jakie parametry żądania? -> advertId, title, description
       - jakie parametry metody? -> @RequestParam Long advertId, @RequestParam String title, @RequestParam String description

     */

//    private static void checkNonNull(Object object) {
//        if (object == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
//    }

    @GetMapping("/edit-advert")
    public String prepareEditAdvertPage(@RequestParam Long advertId, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Advert advert = advertRepository.findByIdAndUserUsername(advertId, username);
        if (advert == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        model.addAttribute("advert", advert);
        return "edit-user-advert-page";
    }

    @PostMapping("/edit-advert")
    /*
    public String processEditAdvertPage(@RequestParam Long advertId, @RequestParam String title, @RequestParam String description)
     */
    public String processEditAdvertPage(@Valid @ModelAttribute("advert") EditAdvertRequest request, Errors errors) {
        log.debug("Edit advert with request: {}", request);
        if (errors.hasErrors()) {
            errors.getFieldErrors().forEach(fieldError -> log.debug("Error in request: {}", fieldError));
            return "edit-user-advert-page";
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Advert advert = advertRepository.findByIdAndUserUsername(request.getId(), username);
        if (advert == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        advert.setTitle(request.getTitle());
        advert.setDescription(request.getDescription());
        advertRepository.save(advert);
        return "redirect:/user-adverts";
    }

}
