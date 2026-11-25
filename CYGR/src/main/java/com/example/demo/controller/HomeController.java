package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.service.FilmService;


@Controller
public class HomeController {

    FilmService filmService;

    public HomeController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("films", filmService.findFilmsALAffiche());
        model.addAttribute("message", "Les films et les acteurs du cinéma");
        return "index";
    }
}
