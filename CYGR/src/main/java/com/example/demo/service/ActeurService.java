package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Acteur;
import java.util.List;
import com.example.demo.repository.ActeurRepository;

@Service 
public class ActeurService {
    
    private final ActeurRepository acteurRepository;

    public ActeurService(ActeurRepository acteurRepository){
        this.acteurRepository = acteurRepository;
    }

    public List<Acteur> findAll(){
        return  acteurRepository.findAll();
    }
}
