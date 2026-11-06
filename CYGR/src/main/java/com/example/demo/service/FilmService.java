package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Acteur;
import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import java.util.List;

@Service 
public class FilmService {
    
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository){
        this.filmRepository = filmRepository;
    }

    public List<Film> findAll(){
        return filmRepository.findAll();
    }

}
