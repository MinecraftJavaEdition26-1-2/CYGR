package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
