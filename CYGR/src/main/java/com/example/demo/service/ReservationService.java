package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Client;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Seance;
import com.example.demo.entity.Statut;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ReservationRepository;
import com.example.demo.repository.SeanceRepository;

@Service
public class ReservationService {

    public static final int DELAI_ANNULATION_HEURES = 2;
    
    private final ReservationRepository reservationRepository;
    private final SeanceRepository seanceRepository;
    private final ClientRepository clientRepository;
    private final CarteFideliteService carteFideliteService;

    public ReservationService(ReservationRepository reservationRepository, SeanceRepository seanceRepository,
                              ClientRepository clientRepository, CarteFideliteService carteFideliteService) {
        this.reservationRepository = reservationRepository;
        this.seanceRepository = seanceRepository;
        this.clientRepository = clientRepository;
        this.carteFideliteService = carteFideliteService;
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

        // Fidélité
        boolean placeOfferte = carteFideliteService.appliquerFidelite(client, nombrePlaces);
        int placesPayantes = placeOfferte ? nombrePlaces - 1 : nombrePlaces;

        // Tout est bon, on enregistre.
        Reservation reservation = new Reservation();
        reservation.setClient(client);
        reservation.setSeance(seance);
        reservation.setNombrePlaces(nombrePlaces);
        reservation.setPlaceOfferte(placeOfferte);
        reservation.setPrixTotal(seance.getPrix().multiply(BigDecimal.valueOf(placesPayantes)));
        reservation.setDateReservation(LocalDateTime.now());
        return reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation annuler(Long clientId, Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RegleMetierException("Réservation introuvable"));

        // la réservation doit appartenir à ce client
        if (!reservation.getClient().getId().equals(clientId)) {
            throw new RegleMetierException("Cette réservation n'appartient pas à ce client");
        }

        // Vérifie le statut
        if (reservation.getStatut() == Statut.ANNULEE) {
            throw new RegleMetierException("Cette réservation est déjà annulée");
        }

        // Vérifie l'heure avant la sceance
        LocalDateTime limite = reservation.getSeance().getDateHeureDebut().minusHours(DELAI_ANNULATION_HEURES);
        if (LocalDateTime.now().isAfter(limite)) {
            throw new RegleMetierException("Annulation impossible, la séance commence dans moins de " + DELAI_ANNULATION_HEURES + " h");
        }

        // Fidélité : peut refuser l'annulation si les points ont déjà été dépensés
        carteFideliteService.annulerFidelite(reservation);

        // tout est bon.
        reservation.setStatut(Statut.ANNULEE);
        return reservationRepository.save(reservation);
    }



    public List<Seance> seancesAVenir() {
        LocalDateTime maintenant = LocalDateTime.now();
        return seanceRepository.findAll().stream()
                .filter(s -> s.getDateHeureDebut().isAfter(maintenant))
                .sorted(Comparator.comparing(Seance::getDateHeureDebut))
                .toList();
    }

    public List<Seance> seancesAVenir(Long filmId, LocalDate date) {
        return seancesAVenir().stream()
                .filter(s -> filmId == null || s.getFilm().getId().equals(filmId))
                .filter(s -> date == null || s.getDateHeureDebut().toLocalDate().equals(date))
                .toList();
    }



}
