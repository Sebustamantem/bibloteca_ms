package com.microservice.libros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.libros.model.Libros;

@Repository
public interface LibrosRepository extends JpaRepository<Libros, Integer> {

    
}

