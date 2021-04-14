package com.projects.ams.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class FavouriteAdvertsController {

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
}
