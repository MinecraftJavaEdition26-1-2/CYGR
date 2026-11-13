package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import com.example.demo.entity.Salle;
import com.example.demo.service.SalleService;

@Controller
@RequestMapping("/salles")
public class SalleController {

    private final SalleService salleService;

    public SalleController(SalleService salleService){
        this.salleService = salleService;
    }

    @GetMapping
    public String liste(Model model){
        model.addAttribute("salles", salleService.findAll());
        return "salles/liste";
    }

    @GetMapping("/{id}")
    public String fiche(@PathVariable Long id, Model model){
        Salle salle = salleService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Salle introuvable"));
        model.addAttribute("salle", salle);
        return "salles/fiche";
    }
}
