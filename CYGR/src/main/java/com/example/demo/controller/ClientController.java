package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;


@Controller 
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
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

    
}
