package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.entity.Film;
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

    @GetMapping("/nouveau") // GET /films/nouveau
    public String nouveau(Model model) {
        model.addAttribute("film", new Film());
        return "films/formulaireAjout";
    }

    @PostMapping("/enregistrer") // POST /films/enregistrer
    public String enregistrer(@ModelAttribute Film film) {
        filmService.enregistrer(film);
        return "redirect:/films";
    }
    
    
    

}
