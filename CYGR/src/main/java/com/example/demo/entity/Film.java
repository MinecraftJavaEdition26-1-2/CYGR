package com.example.demo.entity;

import java.util.HashSet;

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

    @Column(nullable = false)
    private HashSet<Acteur> tabActeurs = new HashSet<Acteur>();

    @Column(nullable = false)
    private Integer anneeSortie;
}
