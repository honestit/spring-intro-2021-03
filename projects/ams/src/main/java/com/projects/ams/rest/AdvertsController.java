package com.projects.ams.rest;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/adverts")
public class AdvertsController {

    private AdvertRepository advertRepository;
    private UserRepository userRepository;

    public AdvertsController(AdvertRepository advertRepository) {
        this.advertRepository = advertRepository;
    }

    @GetMapping
    public List<Advert> getAdverts() {
        return advertRepository.findAll();
    }

    @GetMapping("/{id}")
    public Advert getAdvert(@PathVariable Long id) {
        Optional<Advert> optionalAdvert = advertRepository.findById(id);
        if (optionalAdvert.isPresent()) {
            return optionalAdvert.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User doesn't exist");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAdvert(@PathVariable Long id) {
        Optional<Advert> optionalAdvert = advertRepository.findById(id);
        if (optionalAdvert.isPresent()) {
            Advert advert = optionalAdvert.get();
            Long count = userRepository.countByFavouriteAdvertsContains(advert);
            if (count.equals(0L)) {
                advertRepository.delete(advert);
            }
            else {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
