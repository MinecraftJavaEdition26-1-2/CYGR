package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Salle;
import com.example.demo.repository.SalleRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SalleService {

    private final SalleRepository salleRepository;

    public SalleService(SalleRepository salleRepository){
        this.salleRepository = salleRepository;
    }

    public List<Salle> findAll(){
        return salleRepository.findAll();
    }

    public Optional<Salle> findById(Long id){
        return salleRepository.findById(id);
    }

    public Salle save(Salle salle){
        return salleRepository.save(salle);
    }

    public void deleteById(Long id){
        salleRepository.deleteById(id);
    }
}
