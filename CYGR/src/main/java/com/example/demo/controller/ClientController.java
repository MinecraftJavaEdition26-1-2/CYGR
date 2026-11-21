package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Client;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.service.CarteFideliteService;
import com.example.demo.service.ClientService;
import com.example.demo.service.ReservationService;


@Controller 
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientService clientService;
    private final CarteFideliteService carteFideliteService;
    private final ReservationService reservationService;

    public ClientController(ClientService clientService, CarteFideliteService carteFideliteService, ReservationService reservationService){
        this.clientService = clientService;
        this.carteFideliteService = carteFideliteService;
        this.reservationService = reservationService;
    }
    

    @GetMapping // GET /clients ou /clients?recherche=dupont
    public String liste(@RequestParam(required = false) String recherche, Model model) {
        model.addAttribute("clients", clientService.rechercher(recherche));
        model.addAttribute("recherche", recherche);
        return "clients/liste";     //affiche template/clients/liste.htm
    }

    @GetMapping("/{id}/modifier") // GET /clients/3/modifier
    public String modifier(@PathVariable Long id, Model model) {
        model.addAttribute("clients", clientService.findById(id));
        return "clients/formulaire";
    }


    @GetMapping("/nouveau") // GET /clients/nouveau
    public String nouveau(Model model) {
        model.addAttribute("clients", new Client());
        return "clients/formulaire"; 
    }

    @PostMapping("/enregistrer") // POST /clients/enregistrer
    public String enregistrer(@ModelAttribute Client client) {
        clientService.enregistrer(client);
        return "redirect:/clients";
    }

    @PostMapping("/{id}/supprimer") // POST /clients/3/supprimer
    public String supprimer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientService.supprimer(id);
            redirectAttributes.addFlashAttribute("succes", "Client supprimé");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/clients";
    }

    @GetMapping("/{id}") // GET /clients/3
    public String fiche(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("seances", reservationService.seancesAVenir());
        return "clients/fiche";
    }

    @PostMapping("/{id}/carte") // POST /clients/3/carte
    public String creerCarte(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            carteFideliteService.creerCarte(id);
            redirectAttributes.addFlashAttribute("succes", "Carte de fidélité créée");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/clients/" + id; // retour sur la fiche
    }

    
    @PostMapping("/{id}/reserver") // POST /clients/3/reserver
    public String reserver(@PathVariable Long id, @RequestParam Long seanceId,
                        @RequestParam int nombrePlaces, RedirectAttributes redirectAttributes) {
        try {
            reservationService.reserver(id, seanceId, nombrePlaces);
            redirectAttributes.addFlashAttribute("succes", "Réservation confirmée");
        } catch (RegleMetierException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/clients/" + id;
    }
  
}
