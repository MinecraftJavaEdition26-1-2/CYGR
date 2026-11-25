package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import com.example.demo.repository.SeanceRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service 
public class FilmService {
    
    private final FilmRepository filmRepository;
    private final SeanceRepository seanceRepository;

    public FilmService(FilmRepository filmRepository, SeanceRepository seanceRepository){
        this.filmRepository = filmRepository;
        this.seanceRepository = seanceRepository;
    }

    public List<Film> findAll(){
        return filmRepository.findAll();
    }

    public void enregistrer(Film film){
        filmRepository.save(film);
    }

    public void supprimer(Film film){
        filmRepository.delete(film);
    }

    public void deleteById(long id){
        filmRepository.deleteById(id);
    }

    public Film findById(long id){
        return filmRepository.findById(id).orElseThrow();
    }

    public List<Film> rechercherParNom(String nom){
        return filmRepository.findByNomContainingIgnoreCase(nom);
    }

    public List<Film> findFilmsALAffiche(){
        LocalDateTime maintenant = LocalDateTime.now();
        return seanceRepository.findByDateHeureDebutBetween(maintenant, maintenant.plusDays(7)) // Récupère les séances de la semaine à venir
                .stream() // transforme le tout en stream pour pouvoir les manipuler plus facilement
                .map(seance -> seance.getFilm()) // récupère le film de chaque séance
                .distinct() // supprime les doublons
                .toList(); // transforme le tout en liste
    }

}
