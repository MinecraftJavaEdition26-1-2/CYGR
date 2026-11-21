package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Acteur;
import com.example.demo.entity.Film;
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

    public void enregistrer(Acteur acteur){
        acteurRepository.save(acteur);
    }

    public Acteur findById(long id){
        return acteurRepository.findById(id).orElseThrow();
    }

    // C'est Film qui possède la relation (table film_acteur) : on retire d'abord
    // l'acteur des films où il joue, sinon la suppression échoue (clé étrangère).
    @Transactional
    public void deleteById(long id){
        Acteur acteur = findById(id);
        for (Film film : acteur.getFilms()) {
            film.getActeurs().remove(acteur);
        }
        acteurRepository.delete(acteur);
    }
}
