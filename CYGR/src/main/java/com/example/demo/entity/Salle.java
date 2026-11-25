package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Salle {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @Column(nullable = false, length = 50)
    private String nom;

    @NotNull(message = "La capacité est obligatoire")
    @Positive(message = "La capacité doit être supérieure à 0")
    @Column(nullable = false)
    private Integer capacite;

    @OneToMany(mappedBy = "salle")
    private List<Seance> seances = new ArrayList<>();
}
