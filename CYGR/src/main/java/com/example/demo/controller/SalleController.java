package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.entity.Salle;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.service.SalleService;
import jakarta.validation.Valid;

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

    @GetMapping("/nouvelle")
    public String nouvelle(Model model){
        model.addAttribute("salle", new Salle());
        return "salles/formulaire";
    }

    @GetMapping("/{id}/modifier")
    public String modifier(@PathVariable Long id, Model model){
        Salle salle = salleService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Salle introuvable"));
        model.addAttribute("salle", salle);
        return "salles/formulaire";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute Salle salle, BindingResult resultat){
        if (resultat.hasErrors()) {
            return "salles/formulaire";
        }
        Salle salleEnregistree = salleService.save(salle);
        return "redirect:/salles/" + salleEnregistree.getId();
    }

    @PostMapping("/{id}/supprimer")
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes){
        try {
            salleService.supprimer(id);
            redirectAttributes.addFlashAttribute("succes", "Salle supprimée");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/salles";
    }

    @GetMapping("/{id}")
    public String fiche(@PathVariable Long id, Model model){
        Salle salle = salleService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Salle introuvable"));
        model.addAttribute("salle", salle);
        return "salles/fiche";
    }
}
