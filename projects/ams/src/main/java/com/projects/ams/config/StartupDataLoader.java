package com.projects.ams.config;

import com.projects.ams.model.domain.Advert;
import com.projects.ams.model.domain.User;
import com.projects.ams.model.repositories.AdvertRepository;
import com.projects.ams.model.repositories.UserRepository;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StartupDataLoader {

    private UserRepository userRepository;
    private AdvertRepository advertRepository;

    public StartupDataLoader(UserRepository userRepository, AdvertRepository advertRepository) {
        this.userRepository = userRepository;
        this.advertRepository = advertRepository;
    }

    @EventListener
    public void loadData(ContextRefreshedEvent event) {
        User user = new User(null, "test", "{noop}test", "test", "test", true);
        userRepository.save(user);

        Advert advert1 = new Advert(null, "Kupię psa", "Kupię mądrego psa",
                LocalDateTime.of(2021, 3, 19, 12, 0), user);
        Advert advert2 = new Advert(null, "Sprzedam psa", "Miał być mądry, ale nie jest",
                LocalDateTime.of(2021, 3, 21, 12, 0), user);
        Advert advert3 = new Advert(null, "Kupię kota", "Nie chce psa, ale chcę kota",
                LocalDateTime.of(2021, 3, 10, 12, 0), user);

        advertRepository.save(advert1);
        advertRepository.save(advert2);
        advertRepository.save(advert3);

        User user2 = new User(null, "test2", "{noop}test2", "test2", "test2", true);
        userRepository.save(user2);

        for (int i = 0; i < 10; i++) {
            Advert advert4 = new Advert(null, "Zrobię Springa " + i, "Zrobię Spring jak ktoś nie umie sam", LocalDateTime.of(2021, 4, 12, 0 + i, 0), user2);
            advertRepository.save(advert4);
        }
    }
}
