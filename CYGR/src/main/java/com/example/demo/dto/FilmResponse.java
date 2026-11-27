package com.example.demo.dto;

import com.example.demo.entity.Film;

public record FilmResponse(
    String nom,
    Integer anneeSortie,
    String synopsis,
    Integer age,
    String affiche,
    Integer duree
) {
    public static FilmResponse from(Film f) {
        return new FilmResponse(
            f.getNom(),
            f.getAnneeSortie(),
            f.getSynopsis(),
            f.getAge(),
            f.getAffiche(),
            f.getDuree()
        );
    }
}
