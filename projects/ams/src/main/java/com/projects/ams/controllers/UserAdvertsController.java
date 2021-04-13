package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
}
