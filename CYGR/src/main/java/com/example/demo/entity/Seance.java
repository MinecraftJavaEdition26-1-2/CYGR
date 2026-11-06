package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Entity 
public class Seance {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id; 

    @Column(nullable = false)
    private LocalDateTime dateHeureDebut;

    @Column(nullable = false)
    private Integer prix;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 2)
    private Version version;
}
