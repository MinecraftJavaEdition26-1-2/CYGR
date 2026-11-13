package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nombrePlaces;

    @Column(precision = 10, scale = 2)
    private BigDecimal prixTotal;
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

