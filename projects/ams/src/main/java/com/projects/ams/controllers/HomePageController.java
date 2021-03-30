package com.projects.ams.controllers;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.repositories.AdvertRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@Slf4j
public class HomePageController {

    private AdvertRepository advertRepository;

    public HomePageController(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @GetMapping
    public String prepareHomePage(Model model) {
        List<Advert> adverts = advertRepository.findAllByOrderByPostedDesc();
        log.debug("Collected {} adverts for home page", adverts.size());
        model.addAttribute("adverts", adverts);
        return "home-page";
    }
}
