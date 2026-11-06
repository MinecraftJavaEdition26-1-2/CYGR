package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter 
@Setter 
@Entity 
public class Acteur {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String familyName;

    @ManyToMany(mappedBy = "acteurs")
    private List<Film> films = new ArrayList<Film>();


}
