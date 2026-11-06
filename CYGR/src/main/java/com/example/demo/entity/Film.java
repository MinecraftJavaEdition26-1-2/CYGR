package com.example.demo.entity;

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
}
