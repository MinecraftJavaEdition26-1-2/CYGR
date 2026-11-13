package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.entity.CarteFidelite;
import com.example.demo.repository.CarteFideliteRepository;

@Service
public class CarteFideliteService {
    
    private final CarteFideliteRepository carteFideliteRepository;

    public CarteFideliteService(CarteFideliteRepository carteFideliteRepository){
        this.carteFideliteRepository = carteFideliteRepository;
    }

    public List<CarteFidelite> findAll(){
        return carteFideliteRepository.findAll();
    }
}
