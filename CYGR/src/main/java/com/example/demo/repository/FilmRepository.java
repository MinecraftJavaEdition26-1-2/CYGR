package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Film;
import java.util.List;


public interface FilmRepository extends JpaRepository<Film,Long>{
    List<Film> findByNom(String nom);
}
