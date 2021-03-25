package com.projects.ams.model.domain;

import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Setter @Getter
@ToString(exclude = { "password", })
@EqualsAndHashCode(of = { "username" })
@NoArgsConstructor @AllArgsConstructor
public class User {

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

}
