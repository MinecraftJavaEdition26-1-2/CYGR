package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Client;
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
}
