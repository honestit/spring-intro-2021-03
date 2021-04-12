package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping
    public String prepareHomePage(Model model) {
        List<Advert> adverts = advertRepository.findAllByOrderByPostedDesc();
        String checkMe = "abc";
        log.debug("Collected {} adverts for home page", adverts.size());
        // Wstawiamy do modelu atrybut o nazwie "adverts" (od tej pory na strony home-page.html będziemy mogli
        // korzystać ze zmiennej o tej samej nazwie. Pod tą nazwę podstawiamy wartość czyli naszą listę ogłoszeń.
        model.addAttribute("adverts", adverts);
        return "home-page";
    }

    @PostMapping("/add-advert")
    public String saveAdvert(@RequestParam String title, @RequestParam String description) {
        log.debug("Adding new advert with title = '{}' and description = '{}'", title, description);
        Advert newAdvert = new Advert();
        newAdvert.setTitle(title);
        newAdvert.setDescription(description);
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
