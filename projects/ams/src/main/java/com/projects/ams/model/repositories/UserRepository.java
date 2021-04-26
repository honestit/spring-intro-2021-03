package com.projects.ams.model.repositories;

import com.projects.ams.model.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    User findByUsername(String username);

    // W ten sposób określamy, że pola z encji User wymienione w "attributePaths" mają być pobierane
    // w opcji EAGER (ale tylko dla tego zapytania).
    // Wszystkie pola nie wymienione tutaj są pobierane w normalny sposób (tak jak jest to określone
    // w klasie encji).

    // Fragment nazwy metody "WithFavouriteAdverts" nie ma żadnego znaczenia dla jej działania,
    // ale ładnie wygląda i podkreśla, co metoda robi ;)
    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"favouriteAdverts"})
    User findWithFavouriteAdvertsByUsername(String username);

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = {"roles", "favouriteAdverts"})
    List<User> findAllWithDataBy();
}
