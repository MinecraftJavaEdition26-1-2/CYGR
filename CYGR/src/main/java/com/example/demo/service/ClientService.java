package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Client;
import com.example.demo.exception.RegleMetierException;
import com.example.demo.repository.ClientRepository;

@Service
public class ClientService {
    
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository){
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public void enregistrer(Client client){
        if (client.getId() == null) {          // création
            clientRepository.save(client);
            return;
        }
        Client existant = findById(client.getId());   // modification
        existant.setNom(client.getNom());
        existant.setPrenom(client.getPrenom());
        existant.setEmail(client.getEmail());
        existant.setDateNaissance(client.getDateNaissance());
        existant.setEtudiant(client.isEtudiant());
        clientRepository.save(existant);
    }


    @Transactional
    public void supprimer(Long id){
        Client client = findById(id); // lève une RegleMetierException si l'id n'existe pas

        // on ne supprime pas un client qui a des réservations
        if (!client.getReservations().isEmpty()) {
            throw new RegleMetierException("Impossible de supprimer " + client.getPrenom() + " " + client.getNom()
                    + " : ce client a " + client.getReservations().size() + " réservation(s)");
        }

        clientRepository.delete(client); // la carte de fidélité est supprimée avec.
    }

    public Client findById(Long id) {
        return clientRepository.findById(id).orElseThrow(() -> new RegleMetierException("Client introuvable (id " + id + ")"));
    }


    public List<Client> rechercher(String texte) {
    if (texte == null || texte.isBlank()) {
        return clientRepository.findAll();
    }
    return clientRepository.findByNomContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPrenomContainingIgnoreCase(texte, texte,texte);
}

}
