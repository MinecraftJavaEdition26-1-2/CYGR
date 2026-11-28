package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Salle;
import com.example.demo.entity.Seance;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SalleRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class SalleService {

    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;

    public SalleService(SalleRepository salleRepository, ReservationRepository reservationRepository){
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Salle> findAll(){
        return salleRepository.findAll();
    }

    public Optional<Salle> findById(Long id){
        return salleRepository.findById(id);
    }

    public Salle enregistrer(Salle salle){
        if (salle.getId() != null) {
            Salle salleExistante = salleRepository.findById(salle.getId())
                .orElseThrow(() -> new RegleMetierException("Salle introuvable"));

            for (Seance seance : salleExistante.getSeances()) {
                if (seance.getDateHeureDebut().isBefore(LocalDateTime.now())) {
                    continue;
                }
                Long placesReservees = reservationRepository.totalPlaces(seance.getId());
                if (placesReservees > salle.getCapacite()) {
                    throw new RegleMetierException("Impossible de passer la capacité à " + salle.getCapacite()
                            + " places : la séance de " + seance.getFilm().getNom() + " du "
                            + seance.getDateHeureDebut().format(DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm"))
                            + " a déjà " + placesReservees + " places réservées");
                }
            }
        }

        return salleRepository.save(salle);
    }

    public void supprimer(Long id){
        Salle salle = salleRepository.findById(id)
            .orElseThrow(() -> new RegleMetierException("Salle introuvable"));

        if (!salle.getSeances().isEmpty()) {
            throw new RegleMetierException("Impossible de supprimer " + salle.getNom()
                    + " : cette salle a " + salle.getSeances().size() + " séance(s) programmée(s)");
        }

        salleRepository.delete(salle);
    }
}
