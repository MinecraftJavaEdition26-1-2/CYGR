package com.example.demo.dto;

import com.example.demo.entity.Film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilmRequest(
    @NotNull Long id,
    @NotBlank String nom,
    @NotNull Integer anneeSortie,
    @NotBlank String synopsis,
    @NotNull Integer age,
    @NotBlank String affiche,
    @NotNull Integer duree
) {
    public Film toEntity(){
        Film film = new Film();
        film.setId(id);
        film.setNom(nom);
        film.setAnneeSortie(anneeSortie);
        film.setSynopsis(synopsis);
        film.setAge(age);
        film.setAffiche(affiche);
        film.setDuree(duree);
        return film;
    }
}