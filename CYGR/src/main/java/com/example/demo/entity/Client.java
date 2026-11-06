package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateNaissance;
    private boolean etudiant;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "carte_id")
    private CarteFidelite carte;
}