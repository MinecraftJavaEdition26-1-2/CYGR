
package com.example.demo.repository;

import com.example.demo.entity.Seance;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeanceRepository extends JpaRepository<Seance, Long> {

}