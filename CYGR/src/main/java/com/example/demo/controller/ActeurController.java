package com.example.demo.controller;

import com.example.demo.entity.Acteur;
import com.example.demo.entity.Film;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.ActeurRepository;
import com.example.demo.service.ActeurService;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller 
@RequestMapping("/acteurs")
public class ActeurController {

    private final ActeurRepository acteurRepository;
    private final ActeurService acteurService;

    public ActeurController(ActeurService acteurService, ActeurRepository acteurRepository){
        this.acteurService = acteurService;
        this.acteurRepository = acteurRepository;
    }

    @GetMapping 
    public String liste(Model model){
        model.addAttribute("acteurs", acteurService.findAll());
        return "acteurs/liste";
    }

    @GetMapping("/nouveau") // GET /acteurs/nouveau
    public String nouveau(Model model) {
        model.addAttribute("acteur", new Acteur());
        return "acteurs/formulaireAjout";
    }

    @PostMapping("/enregistrer") // POST /acteurs/enregistrer
    public String enregistrer(@ModelAttribute Acteur acteur, RedirectAttributes redirectAttributes) {
        boolean creation = acteur.getId() == 0; // id est un long primitif : 0 = nouvel acteur
        try {
            acteurService.enregistrer(acteur);
            redirectAttributes.addFlashAttribute("succes", creation ? "Acteur ajouté" : "Acteur modifié");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/acteurs";
    }

    @GetMapping("/formulaireSupprimer") // GET /acteurs/formulaireSupprimer
    public String formulaireSupprimer(Model model) {
        model.addAttribute("acteurs", acteurService.findAll());
        return "acteurs/formulaireSupprimer";
    }

    @PostMapping("/supprimer") // POST /acteurs/supprimer
    public String supprimer(@RequestParam long id, RedirectAttributes redirectAttributes) {
        try {
            acteurService.deleteById(id);
            redirectAttributes.addFlashAttribute("succes", "Acteur supprimé");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/acteurs";
    }

    @GetMapping("/formulaireModifier") // GET /acteurs/formulaireModifier
    public String formulaireModifier(Model model) {
        model.addAttribute("acteurs", acteurService.findAll());
        return "acteurs/formulaireModifier";
    }

    @GetMapping("/modifierActeur") // GET /acteurs/modifierActeur?id=...
    public String modifierActeur(@RequestParam long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("acteur", acteurService.findById(id));
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/acteurs"; // acteur introuvable : retour à la liste avec le message
        }
        return "acteurs/modifierActeur";
    }

    @GetMapping("/voirFilms") // GET /acteurs/voirFilms?id=...
    public String voirFilms(@RequestParam long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Acteur acteur = acteurService.findById(id);
            List<Film> films = acteur.getFilms();
            model.addAttribute("films", films);
            model.addAttribute("acteur", acteur);
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/acteurs";
        }
        return "acteurs/listeFilms"; // affiche templates/acteurs/listeFilms.html
    }

}