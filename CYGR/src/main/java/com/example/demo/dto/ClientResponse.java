package com.example.demo.dto;

import java.time.LocalDate;
import com.example.demo.entity.CarteFidelite;
import com.example.demo.entity.Client;

//ce que l'API renvoie
public record ClientResponse(
        Long id, String nom, String prenom, String email, LocalDate dateNaissance, boolean etudiant,
        String numeroCarte,       // null si pas de carte
        Integer pointsFidelite,   // null si pas de carte
        int nombreReservations) {

    public static ClientResponse from(Client c) {
        CarteFidelite carte = c.getCarte();
        return new ClientResponse(
                c.getId(), c.getNom(), c.getPrenom(), c.getEmail(), c.getDateNaissance(), c.isEtudiant(),
                carte != null ? carte.getNumero() : null,
                carte != null ? carte.getPoints() : null,
                c.getReservations().size());
    }
}