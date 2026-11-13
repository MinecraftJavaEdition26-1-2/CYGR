package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nombrePlaces;
    private Integer prixTotal;
    private LocalDateTime dateReservation;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.CONFIRMEE;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    
    @ManyToOne
    @JoinColumn(name = "seance_id")
    private Seance seance;
    
}

