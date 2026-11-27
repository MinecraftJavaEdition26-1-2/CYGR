package com.example.demo.api;

import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.dto.ClientRequest;
import com.example.demo.dto.ClientResponse;
import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientApiController {

    private final ClientService clientService;

    public ClientApiController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping   // GET /api/v1/clients ou /api/v1/clients?recherche=dupont
    public List<ClientResponse> lister(@RequestParam(required = false) String recherche) {
        return clientService.rechercher(recherche).stream().map(ClientResponse::from).toList();
    }

    @GetMapping("/{id}")
    public ClientResponse lire(@PathVariable Long id) {
        return ClientResponse.from(clientService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClientResponse> creer(@Valid @RequestBody ClientRequest request) {
        Client client = clientService.enregistrer(request.toEntity());
        return ResponseEntity.created(URI.create("/api/v1/clients/" + client.getId()))
                .body(ClientResponse.from(client));
    }

    @PutMapping("/{id}")
    public ClientResponse modifier(@PathVariable Long id, @Valid @RequestBody ClientRequest request) {
        Client client = request.toEntity();
        client.setId(id);   // enregistrer() passe en mode "modification" et renvoie 404 si l'id n'existe pas
        return ClientResponse.from(clientService.enregistrer(client));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        clientService.supprimer(id);
    }
}