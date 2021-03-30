package com.projects.ams.model.repositories;

import com.projects.ams.model.domain.Advert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertRepository extends JpaRepository<Advert, Long> {

    List<Advert> findAllByOrderByPostedDesc();
}
