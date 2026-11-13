package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;


@Controller 
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientService clientService;

    public ClientController(ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping // GET /clients
    public String liste(Model model){
        model.addAttribute("clients", clientService.findAll());
        return "clients/liste"; //affiche template/films/liste.html
    }

    @GetMapping("/nouveau") // GET /clients/nouveau
    public String nouveau(Model model) {
        model.addAttribute("clients", new Client());
        return "clients/formulaireAjoutClient";
    }

    @PostMapping("/enregistrer") // POST /clients/enregistrer
    public String enregistrer(@ModelAttribute Client client) {
        clientService.enregistrer(client);
        return "redirect:/clients";
    }

    
}
