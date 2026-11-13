package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Entity 
public class Seance {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; 

    @Column(nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(nullable = false)
    private Integer prix;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Version version;

    @ManyToOne(optional = false)
    @JoinColumn(name = "film_id", nullable = false)
    private Film film;

    @ManyToOne(optional = false)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    @OneToMany(mappedBy = "seance")
    private List<Reservation> reservations = new ArrayList<>();
}
