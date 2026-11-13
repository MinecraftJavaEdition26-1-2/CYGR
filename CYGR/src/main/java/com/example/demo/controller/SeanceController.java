package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.service.SeanceService;

@Controller
@RequestMapping("/seances")
public class SeanceController {

    private final SeanceService seanceService;

    public SeanceController(SeanceService seanceService){
        this.seanceService = seanceService;
    }

    @GetMapping
    public String liste(Model model){
        model.addAttribute("seances", seanceService.findAll());
        return "seances/liste";
    }
}
