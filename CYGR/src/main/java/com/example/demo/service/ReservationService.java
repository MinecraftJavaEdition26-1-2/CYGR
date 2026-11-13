package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Client;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Seance;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SeanceRepository;

@Service
public class ReservationService {
    
    private final ReservationRepository reservationRepository;
    private final SeanceRepository seanceRepository;
    private final ClientRepository clientRepository;

    public ReservationService(ReservationRepository reservationRepository, SeanceRepository seanceRepository, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.seanceRepository = seanceRepository;
        this.clientRepository = clientRepository;
    }

    //nb place restante pour cette sceance
    public int placesRestantes(Seance seance) {
        Long reservees = reservationRepository.totalPlaces(seance.getId());
        return seance.getSalle().getCapacite() - reservees.intValue();
    }

    @Transactional
    public Reservation reserver(Long clientId, Long seanceId, int nombrePlaces) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RegleMetierException("Client introuvable"));
        Seance seance = seanceRepository.findById(seanceId).orElseThrow(() -> new RegleMetierException("Séance introuvable"));
    
        // Regle 1, au moins une place
        if (nombrePlaces < 1) {
            throw new RegleMetierException("Il faut réserver au moins une place");
        }

        // Regle 2, pas de surréservation
        int restantes = placesRestantes(seance);
        if (nombrePlaces > restantes) {
            throw new RegleMetierException("Il ne reste que " + restantes + " place(s)");
        }

        // Regle 3, age minimum du film
        int age = Period.between(client.getDateNaissance(), LocalDate.now()).getYears();
        Integer ageMinimum = seance.getFilm().getAge();
        if (ageMinimum != null && age < ageMinimum) {
            throw new RegleMetierException("Film interdit aux moins de " + ageMinimum + " ans");
        }

        // Tout est bon, on enregistre.
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setSeance(seance);
        reservation.setNombrePlaces(nombrePlaces);
        reservation.setPrixTotal(nombrePlaces * seance.getPrix());
        reservation.setDateReservation(LocalDateTime.now());
        return reservationRepository.save(reservation);
        }


}
