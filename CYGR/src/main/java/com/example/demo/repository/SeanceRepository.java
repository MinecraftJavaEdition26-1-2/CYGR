
package com.example.demo.repository;

import com.example.demo.entity.Seance;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findAllByOrderByDateHeureDebutAsc();

    // Fonction qui permet de récupérer toutes les séances dont la date de début est comprise entre les deux dates passées en paramètre
    List<Seance> findByDateHeureDebutBetween(LocalDateTime debut, LocalDateTime fin);

    // true si au moins une séance utilise ce film
    boolean existsByFilmId(Long filmId);

}
