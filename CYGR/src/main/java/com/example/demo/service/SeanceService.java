package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Film;
import com.example.demo.entity.Salle;
import com.example.demo.entity.Seance;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.FilmRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SalleRepository;
import com.example.demo.repository.SeanceRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final FilmRepository filmRepository;
    private final SalleRepository salleRepository;
    private final ReservationRepository reservationRepository;

    public SeanceService(SeanceRepository seanceRepository, FilmRepository filmRepository, SalleRepository salleRepository,
                         ReservationRepository reservationRepository){
        this.seanceRepository = seanceRepository;
        this.filmRepository = filmRepository;
        this.salleRepository = salleRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<Seance> findAll(){
        return seanceRepository.findAllByOrderByDateHeureDebutAsc();
    }

    public Optional<Seance> findById(Long id){
        return seanceRepository.findById(id);
    }

    public Seance save(Seance seance){
        return seanceRepository.save(seance);
    }

    public Seance enregistrer(Seance seance, Long filmId, Long salleId){
        if (seance.getDateHeureDebut().isBefore(LocalDateTime.now())) {
            throw new RegleMetierException("Impossible de programmer une séance dans le passé");
        }

        Film film = filmRepository.findById(filmId)
            .orElseThrow(() -> new RegleMetierException("Film introuvable"));
        Salle salle = salleRepository.findById(salleId)
            .orElseThrow(() -> new RegleMetierException("Salle introuvable"));

        if (seance.getId() != null) {
            Long placesReservees = reservationRepository.totalPlaces(seance.getId());
            if (placesReservees > salle.getCapacite()) {
                throw new RegleMetierException("Impossible de placer la séance dans " + salle.getNom()
                        + " : " + placesReservees + " places sont déjà réservées pour seulement "
                        + salle.getCapacite() + " places dans la salle");
            }
        }

        seance.setFilm(film);
        seance.setSalle(salle);
        return seanceRepository.save(seance);
    }

    public void deleteById(Long id){
        seanceRepository.deleteById(id);
    }
}
