package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.entity.Seance;
import com.example.demo.entity.Version;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.service.FilmService;
import com.example.demo.service.SalleService;
import com.example.demo.service.SeanceService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/seances")
public class SeanceController {

    private final SeanceService seanceService;
    private final FilmService filmService;
    private final SalleService salleService;

    public SeanceController(SeanceService seanceService, FilmService filmService, SalleService salleService){
        this.seanceService = seanceService;
        this.filmService = filmService;
        this.salleService = salleService;
    }

    @GetMapping
    public String liste(Model model){
        model.addAttribute("seances", seanceService.findAll());
        return "seances/liste";
    }

    @GetMapping("/nouvelle")
    public String nouvelle(Model model){
        model.addAttribute("seance", new Seance());
        ajouterListes(model);
        return "seances/formulaire";
    }

    @PostMapping("/enregistrer")
    public String enregistrer(@Valid @ModelAttribute Seance seance, BindingResult resultat,
                              @RequestParam(required = false) Long filmId, @RequestParam(required = false) Long salleId,
                              Model model, RedirectAttributes redirectAttributes){
        if (filmId == null) {
            resultat.rejectValue("film", "obligatoire", "Le film est obligatoire");
        }
        if (salleId == null) {
            resultat.rejectValue("salle", "obligatoire", "La salle est obligatoire");
        }

        if (!resultat.hasErrors()) {
            try {
                seanceService.enregistrer(seance, filmId, salleId);
                redirectAttributes.addFlashAttribute("succes", "Séance enregistrée");
                return "redirect:/seances";
            } catch (RegleMetierException e) {
                model.addAttribute("erreur", e.getMessage());
            }
        }

        model.addAttribute("filmId", filmId);
        model.addAttribute("salleId", salleId);
        ajouterListes(model);
        return "seances/formulaire";
    }

    private void ajouterListes(Model model){
        model.addAttribute("films", filmService.findAll());
        model.addAttribute("salles", salleService.findAll());
        model.addAttribute("versions", Version.values());
    }
}
