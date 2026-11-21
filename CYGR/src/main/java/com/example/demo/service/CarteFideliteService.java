package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.CarteFidelite;
import com.example.demo.entity.Client;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.CarteFideliteRepository;
import com.example.demo.repository.ClientRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class CarteFideliteService {
    
    private final CarteFideliteRepository carteFideliteRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;

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

}
