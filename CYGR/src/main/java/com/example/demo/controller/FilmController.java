package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.entity.Acteur;
import com.example.demo.entity.Film;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.service.FilmService;
import com.example.demo.service.ActeurService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;





@Controller 
@RequestMapping("/films")
public class FilmController {
    
    private final ActeurController acteurController;
    private final FilmService filmService;
    private final ActeurService acteurService;

    public FilmController(FilmService filmService, ActeurService acteurService, ActeurController acteurController){
        this.filmService = filmService;
        this.acteurService = acteurService;
        this.acteurController = acteurController;
    }

    @GetMapping //GET /films
    public String liste(Model model){
        model.addAttribute("films", filmService.findAll());
        return "films/liste"; //affiche template/films/liste.html
    }

    @GetMapping("/detail") // GET /films/detail?id=...
    public String detail(@RequestParam long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("film", filmService.findById(id));
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/films"; // film introuvable : retour à la liste avec le message
        }
        return "films/detail";
    }

    @GetMapping("/nouveau") // GET /films/nouveau
    public String nouveau(Model model) {
        model.addAttribute("film", new Film());
        return "films/formulaireAjout";
    }

    @PostMapping("/enregistrer") // POST /films/enregistrer
    public String enregistrer(@ModelAttribute Film film, RedirectAttributes redirectAttributes) {
        boolean creation = film.getId() == null; // pas encore d'id = nouveau film
        try{
            filmService.enregistrer(film);
            redirectAttributes.addFlashAttribute("succes", creation ? "Film ajouté" : "Film modifié");
        }catch (RegleMetierException e){
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/films";
    }

    @GetMapping("/formulaireSupprimer") // GET /films/formulaireSupprimer
    public String formulaireSupprimer(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films/formulaireSupprimer";
    }

    @PostMapping("/supprimer") // POST /films/supprimer
    public String supprimer(@RequestParam long id, RedirectAttributes redirectAttributes) {
        try {
            filmService.deleteById(id);
            redirectAttributes.addFlashAttribute("succes", "Film supprimé");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/films";
    }

    @GetMapping("/formulaireModifier") // GET /films/formulaireModifier
    public String formulaireModifier(Model model) {
        model.addAttribute("films", filmService.findAll());
        return "films/formulaireModifier";
    }

    @GetMapping("/modifierFilm") // GET /films/modifierFilm
    public String modifierFilm(@RequestParam long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute("film", filmService.findById(id));
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/films";
        }
        return "films/modifierFilm";
    }

    @GetMapping("/ajouterActeur")
    public String ajouterActeur(@RequestParam long id, Model model, RedirectAttributes redirectAttributes) {
        try{
            Film film = filmService.findById(id);
            /*On récupère les acteurs qui ne sont pas déjà dans le film*/
            List<Acteur> acteurDispo = acteurService.findAll(). // On récupère tous les acteurs présent dans la bdd
            stream(). // transforme la liste en stream ce qui permet de trier sans écrire les boucles sois meme 
                filter(a -> !film.getActeurs().contains(a)) // on filtre pour garder que les acteurs qui ne sont pas encore dans la liste des acteurs du film
                /*
                    On garde seulement les élement qui font en sorte que a soit true
                    a est l'acteur en court de traitement
                    film.getActeur récupère tous les acteurs déjà présent dans le film
                    contain(a) renvoie true si l'acteur qu'on test est contenu dans film.getActeurs
                */
                .toList();
            List<Acteur> acteurDejaDansFilm = acteurService.findAll().
                stream().
                filter(a -> film.getActeurs().contains(a))
                .toList();
            model.addAttribute("film", film);
            model.addAttribute("acteursDispo", acteurDispo);
            model.addAttribute("acteurDansFilm", acteurDejaDansFilm);
            // pas de message de succès ici : on ne fait qu'afficher une page
        }catch(RegleMetierException e){
            // un flash attribute n'est lu qu'après une redirection, donc on redirige
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/films";
        }

        return "films/ajouterActeur";
    }
    
    
    @PostMapping("/enregistrerNouveauActeur")
    public String enregistrerNouveauActeur(@RequestParam long idFilm,@RequestParam(required = false) List<Long> idActeurs, RedirectAttributes redirectAttributes) {
        
        try{
            Film film = filmService.findById(idFilm);
            if (idActeurs != null) { // null si aucune case n'a été cochée
                for (Long idActeur : idActeurs) {
                    Acteur acteur = acteurService.findById(idActeur);
                    film.getActeurs().add(acteur);
                }
                filmService.enregistrer(film);
            }
            redirectAttributes.addFlashAttribute("succes", "Acteur(s) ajouté(s) au film");
        }catch(RegleMetierException e){
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/films/detail?id=" + idFilm;
    }
    
    @PostMapping("/supprimerActeurFilm")
    public String supprimerActeurFilm(@RequestParam long idFilm, @RequestParam(required = false) List<Long> idActeurs,RedirectAttributes redirectAttributes) {
        try {
            Film film = filmService.findById(idFilm);
            if (idActeurs != null) { // null si aucune case n'a été cochée
                for (Long idActeur : idActeurs) {
                    Acteur acteur = acteurService.findById(idActeur);
                    film.getActeurs().remove(acteur);
                }
                filmService.enregistrer(film);
            }
            redirectAttributes.addFlashAttribute("succes", "Acteur(s) retiré(s) du film");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }

        return "redirect:/films/detail?id=" + idFilm;
    }
    
    @GetMapping("/rechercher") // GET /films/rechercher?nom=...
    public String rechercher(@RequestParam String nom, Model model) {
        List<Film> films = filmService.rechercherParNom(nom);
        model.addAttribute("films", films);
        return "films/liste";
    }
    
    
    
    

}
