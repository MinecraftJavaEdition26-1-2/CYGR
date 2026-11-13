package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CarteFidelite {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String numero;
    private Integer points = 0;
    private LocalDate dateCreation;

    @OneToOne(mappedBy = "carte")
    private Client client;

}