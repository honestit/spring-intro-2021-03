package com.projects.ams.model.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adverts")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"title", "posted", "user"})
@NoArgsConstructor
@AllArgsConstructor
public class Advert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 1012)
    private String description;
    private LocalDateTime posted;

    @ManyToOne(optional = false)
    private User user;
}
