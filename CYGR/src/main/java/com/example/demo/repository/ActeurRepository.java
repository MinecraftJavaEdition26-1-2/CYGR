package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Acteur;

public interface ActeurRepository extends JpaRepository<Acteur,Long>{
    
}
