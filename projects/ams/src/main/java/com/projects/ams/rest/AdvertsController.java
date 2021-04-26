package com.projects.ams.rest;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public Advert getUser(@PathVariable Long id) {
        Optional<Advert> optionalAdvert = advertRepository.findById(id);
        if (optionalAdvert.isPresent()) {
            return optionalAdvert.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
    }
}
