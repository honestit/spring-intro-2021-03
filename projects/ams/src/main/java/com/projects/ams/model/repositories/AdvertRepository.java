package com.projects.ams.model.repositories;

import com.projects.ams.model.domain.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdvertRepository extends JpaRepository<Advert, Long> {

    List<Advert> findAllByOrderByPostedDesc();

    List<Advert> findFirst10ByOrderByPostedDesc();

    @Query(nativeQuery = true, value = "SELECT * FROM adverts ORDER BY posted DESC LIMIT 10")
    List<Advert> getAdvertsForAnonymousUser();

    @Query(nativeQuery = true, value = "SELECT DISTINCT title FROM adverts")
    List<String> getAdvertTitles();

    List<Advert> findAllByUserUsernameOrderByPostedDesc(String username);

    Advert findByIdAndUserUsername(Long id, String username);

    Long countByUserId(Long userId);
}
