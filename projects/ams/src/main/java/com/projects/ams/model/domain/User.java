package com.projects.ams.model.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@Setter @Getter
@ToString(exclude = { "password", "favouriteAdverts"})
@EqualsAndHashCode(of = { "username" })
@NoArgsConstructor @AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private Boolean active = Boolean.FALSE;

    @ElementCollection
    @CollectionTable(name = "users_roles",
        joinColumns = @JoinColumn(name = "username", referencedColumnName = "username")
    )
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

//    @ManyToMany(fetch = FetchType.EAGER) -> tego nie należy używać, bo to chamskie rozwiązanie ;)
    // zamiast tego zobacz metodę findUserWithFavouriteAdvertsByUsername w UserRepository
    @ManyToMany
    private Set<Advert> favouriteAdverts = new HashSet<>();

}
