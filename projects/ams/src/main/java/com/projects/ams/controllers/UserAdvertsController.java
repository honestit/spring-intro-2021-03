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
    Usuwanie og??oszenia made by JAVA POL 64

    1. Metoda do usuwania og??oszenia na podstawie id (doda?? do repozytorium AdvertRepository)

      -> Lepsze rozwi??zanie to pobra?? po id og??oszenie, zastanowi?? si?? co zrobi??, je??eli takie si?? nie znajdzie, a jak si?? znajdzie to usun???? (metoda `delete(Advert)` w repozytorium)

    2. Na stronie HTML (user-adverts-page.html) doda?? link/guzik do usuni??cia og??oszenia
        - ??cie??ka linku: /delete-advert
        - potrzebujemy parametru ????dania o nazwie "id" albo "advertId"
        - czyli ostateczna posta?? linku np. dla og??oszenia o id = 10 b??dzie wygl??da??a tak:

          /delete-advert?advertId=10

    3. W kontrolerze UserAdvertsController dodajemy now?? metod?? do obs??ugi ????dania usuni??cia og??oszenia
        - jaka adnotacja do mapowania? -> @PostMapping
        - jaka ??cie??ka? -> /delete-advert
        - jakie parametry ????dania? -> advertId
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

    Edycja og??oszenia made by JAVA POL 64 ???

    1. Doda?? guzik (button w formularzu) na li??cie og??osze?? u??ytkownika
       - musimy przes??a?? id og??oszenia, kt??re edytujemy -> input o typie hidden, nazwie "advertId" i warto??ci r??wnej id og??oszenia

    2. Nowa strona z formularzem do edycji og??oszenia (edit-user-advert-page.html)
       - jak?? metod?? wysy??amy formularz? -> post
       - jaka akcja w formularzu? -> th:action="@{/edit-advert}"
       - jakie pola? ->
            - input typu hidden z nazw?? "advertId",
            - input dla wprowadzenia title (u??y?? atrybuty value, a dok??adnie to th:value="${advert.title}")
            - textarea dla wprowadzenia description (j.w.)
            - guzik do edycji (o typie "submit")
       - link do powrotu do listy og??osze?? (w CSSach zrobimy, aby by?? pi??kny jak guzik)

    3. Potrzebujemy obs??ugi ????dania wy??wietlenia strony z formularzem do edycji
       - jaka ??cie??ka? -> "/edit-advert"
       - jakie mapowanie? -> @GetMapping
       - co zwracamy? -> return "edit-user-advert-page"
       - jakie parametry ????dania? -> advertId
       - jakie parametry metody? -> @RequestParam Long advertId, Model model
       - w jaki spos??b przygotujemy dane dla widoku? ->
          - pobieramy og??oszenie na podstawie advertId + username (zalogowany u??ytkownik)
          - wstawiamy atrybut do modelu reprezentuj??cy og??oszenie do edycji (kt??re przed chwil?? pobrali??my z repozytorium)

    4. Potrzebujemy obs??ugi ????dania edycji og??oszenia w kontrolerze UserAdvertsController
       - jaka ??cie??ka? -> "/edit-advert"
       - jakie mapowanie? -> @PostMapping
       - co zwracamy? -> return "redirect:/user-adverts"
       - jak zedytujemy? ->
            - pobieramy og??oszenie na podstawie advertId + username (zalogowany u??ytkownik)
            - zamieniamy tytu?? i opis na nowe
            - zapis w repozytorium (advertRepository.save)
       - jakie parametry ????dania? -> advertId, title, description
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
