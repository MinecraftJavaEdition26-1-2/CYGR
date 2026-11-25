package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Reservation;


public interface ReservationRepository extends JpaRepository<Reservation,Long>{

    // somme des places réservées pour une séance
    @Query("SELECT COALESCE(SUM(r.nombrePlaces), 0) FROM Reservation r " +
           "WHERE r.seance.id = :seanceId " +
           "AND r.statut = com.example.demo.entity.Statut.CONFIRMEE")
    Long totalPlaces(@Param("seanceId") Long seanceId);

}
