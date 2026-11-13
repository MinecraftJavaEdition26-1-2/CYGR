package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Seance;
import com.example.demo.repository.SeanceRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SeanceService {

    private final SeanceRepository seanceRepository;

    public SeanceService(SeanceRepository seanceRepository){
        this.seanceRepository = seanceRepository;
    }

    public List<Seance> findAll(){
        return seanceRepository.findAll();
    }

    public Optional<Seance> findById(Long id){
        return seanceRepository.findById(id);
    }

    public Seance save(Seance seance){
        return seanceRepository.save(seance);
    }

    public void deleteById(Long id){
        seanceRepository.deleteById(id);
    }
}
