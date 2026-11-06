package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.service.FilmService;


@Controller 
@RequestMapping("/films")
public class FilmController {
    
    private final FilmService filmService;

    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping //GET /films
    public String liste(Model model){
        model.addAttribute("films", filmService.findAll());
        return "films/liste"; //affiche template/films/liste.html
    }
    

}
