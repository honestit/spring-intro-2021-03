package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Controller
@Slf4j
public class FavouriteAdvertsController {

    private UserRepository userRepository;
    private AdvertRepository advertRepository;

    public FavouriteAdvertsController(UserRepository userRepository, AdvertRepository advertRepository) {
        this.userRepository = userRepository;
        this.advertRepository = advertRepository;
    }

    /*

    Lista ulubionych ogłoszeń i dodawanie ogłoszeń do tej listy

    1. Metody udostępniająca widok z listą ulubionych ogłoszeń
       - jakie mapowanie? -> @GetMapping
       - jaka ścieżka? -> /favourite-adverts
       - parametry żądania? -> brak
       - parametry metody? -> Model model
       - co zwracamy? -> return "favourite-adverts-page"
       - jak pobierzemy ulubione ogłoszenia? ->
         - potrzebujemy relacji od encji User *-* Advert
         - pobieramy User na podstawie zalogownay username
         - List<Advert> favouriteAdverts = user.getFavouriteAdverts();
         - listę wstawiamy do model ( alternatywnie: model.addAttribute("adverts", user.getFavouriteAdverts()))

    */
    @GetMapping("/favourite-adverts")
    public String getFavouriteAdverts(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findWithFavouriteAdvertsByUsername(username);
        Set<Advert> favouriteAdverts = user.getFavouriteAdverts();
        model.addAttribute("adverts", favouriteAdverts);
        return "favourite-adverts-page";
    }

    /*

     2. Widok "favourite-adverts-page" analogiczny (jak nie identyczny) do "some-user-adverts-page".

     3. Metoda dodająca ogłoszenie do ulubionych
        - jakie mapowanie? -> @PostMapping
        - jaka ścieżka? -> "/favourite-advert"
        - parametry żądania? -> advertId (id ogłoszenia, które chcemy polubić)
        - parametry metody? -> @RequestParam Long advertId
        - co zwracamy? -> return "redirect:/"
        - jak to zrobimy?
          - pobieramy ogłoszenie na podstawie advertId
          - pobieramy User na podstawie zalogowany username
          - dodajemy ogłoszenie do listy ulubionych ogłoszeń użytkownika: user.getFavouriteAdverts().add(advert);

          */

    @PostMapping("/favourite-adverts")
    public String addFavouriteAdverts(@RequestParam Long advertId, @RequestHeader("Referer") String referer) {
        Optional<Advert> optionalAdvert = advertRepository.findById(advertId);
        // optionalAdvert.isPresent() zanim optionalAdvert.get()
        // optionalAdvert.orElse , orElseGet, orElseThrow, orElseThrow(...)
        Advert advert = optionalAdvert.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findWithFavouriteAdvertsByUsername(username);

        user.getFavouriteAdverts().add(advert);
        userRepository.save(user);

        if (referer == null) {
            return "redirect:/";
        } else {
            log.debug("Referer: {}", referer);
            return "redirect:" + referer;
        }
    }

    /*

      4. Na stronie głównej (home-page.html) przycisk (a'la serduszko) do polubienia ogłoszenia
         - dostępny dla ogłoszeń, które nie są ogłoszeniami zalogowanego użytkownika

     */
}
