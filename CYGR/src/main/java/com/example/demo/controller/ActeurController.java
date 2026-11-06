package com.example.demo.controller;

import com.example.demo.repository.ActeurRepository;
import com.example.demo.service.ActeurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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


    
}
