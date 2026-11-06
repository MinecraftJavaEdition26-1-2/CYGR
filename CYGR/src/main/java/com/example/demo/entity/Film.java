package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Entity 
public class Film {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nom;

    @ManyToMany
    @JoinTable(name = "film_acteur",
        joinColumns = @JoinColumn(name = "film_id"),
        inverseJoinColumns = @JoinColumn(name = "acteur_id"))
    private List<Acteur> acteurs = new ArrayList<Acteur>();

    @Column(nullable = false)
    private Integer anneeSortie;

    @Column 
    private Integer duree;

    @Column(nullable = false, length = 1000)
    private String synopsis;

    @Column(nullable = false)
    private Integer age;

    // Représente l'url à laquel on peut retrouver l'affiche
    @Column(nullable = false, length = 100)
    private String affiche;
}
