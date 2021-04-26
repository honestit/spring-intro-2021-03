package com.projects.ams.rest;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.repositories.AdvertRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/adverts")
public class AdvertsController {

    private AdvertRepository advertRepository;

    public AdvertsController(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @GetMapping
    public List<Advert> getAdverts() {
        return advertRepository.findAll();
    }
}
