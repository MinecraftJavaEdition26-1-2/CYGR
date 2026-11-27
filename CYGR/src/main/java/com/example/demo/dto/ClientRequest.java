package com.example.demo.dto;

import java.time.LocalDate;
import com.example.demo.entity.Client;
import jakarta.validation.constraints.*;

//ce que le client HTTP envoie, pas de "id" et "isAdmin" pour pas se mettre admin via l'API
public record ClientRequest(
        @NotBlank String nom,
        @NotBlank String prenom,
        @NotBlank @Email String email,
        @NotNull @Past LocalDate dateNaissance,
        boolean etudiant) {

    public Client toEntity() {
        Client client = new Client();
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setEmail(email);
        client.setDateNaissance(dateNaissance);
        client.setEtudiant(etudiant);
        return client;
    }
}