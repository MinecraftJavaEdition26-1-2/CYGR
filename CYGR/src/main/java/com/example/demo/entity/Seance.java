package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Entity 
public class Seance {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id; 

    @NotNull(message = "La date et l'heure sont obligatoires")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime dateHeureDebut;

    @NotNull(message = "Le prix est obligatoire")
    @Positive(message = "Le prix doit être supérieur à 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @NotNull(message = "La version est obligatoire")
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
