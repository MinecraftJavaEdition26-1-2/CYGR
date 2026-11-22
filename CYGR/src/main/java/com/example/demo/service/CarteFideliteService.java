package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CarteFidelite;
import com.example.demo.entity.Client;
import com.example.demo.entity.Reservation;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.CarteFideliteRepository;
import com.example.demo.repository.ClientRepository;

@Service
public class CarteFideliteService {
    
    private final CarteFideliteRepository carteFideliteRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    public static final int POINTS_PAR_PLACE = 10;
    public static final int SEUIL_PLACE_OFFERTE = 100;


    public CarteFideliteService(CarteFideliteRepository carteFideliteRepository, ClientService clientService, ClientRepository clientRepository){
        this.carteFideliteRepository = carteFideliteRepository;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
    }

    public List<CarteFidelite> findAll(){
        return carteFideliteRepository.findAll();
    }

    @Transactional
    public void creerCarte(Long clientId) {
        Client client = clientService.findById(clientId);

        // Règle : une seule carte par client (relation 1-1)
        if (client.getCarte() != null) {
            throw new RegleMetierException("Ce client a déjà une carte de fidélité");
        }

        CarteFidelite carte = new CarteFidelite();
        carte.setNumero(String.format("CF-%06d", client.getId())); // ex : CF-000003
        carte.setPoints(0);
        carte.setDateCreation(LocalDate.now());

        // On relie les deux côtés de la relation
        client.setCarte(carte);
        carte.setClient(client);

        clientRepository.save(client); // la carte est enregistrée avec (cascade ALL)
    }



    //retourne true si une place est offerte, 10 points par place reserver et a 100 points une place offerte.
    @Transactional
    public boolean appliquerFidelite(Client client, int nombrePlaces) {
        CarteFidelite carte = client.getCarte();

        // Pas de carte : pas de points, pas de place offerte
        if (carte == null) {
            return false;
        }

        // 1) Place offerte si le seuil est atteint (avec les points d'AVANT cette réservation)
        boolean placeOfferte = carte.getPoints() >= SEUIL_PLACE_OFFERTE;
        if (placeOfferte) {
            carte.setPoints(carte.getPoints() - SEUIL_PLACE_OFFERTE);
        }

        // 2) Points crédités sur les places payées uniquement
        int placesPayees = placeOfferte ? nombrePlaces - 1 : nombrePlaces;
        carte.setPoints(carte.getPoints() + placesPayees * POINTS_PAR_PLACE);

        carteFideliteRepository.save(carte);
        return placeOfferte;
    }


    //retire une réservation ainsi que les points de fideliter qui était associé.     
    @Transactional
    public void annulerFidelite(Reservation reservation) {
        CarteFidelite carte = reservation.getClient().getCarte();
        if (carte == null) {
            return;
        }

        boolean placeOfferte = Boolean.TRUE.equals(reservation.getPlaceOfferte());
        int placesPayees = placeOfferte ? reservation.getNombrePlaces() - 1 : reservation.getNombrePlaces();

        int points = carte.getPoints() - placesPayees * POINTS_PAR_PLACE;
        if (placeOfferte) {
            points += SEUIL_PLACE_OFFERTE;
        }

        carte.setPoints(Math.max(0, points));
        carteFideliteRepository.save(carte);
    }


}
