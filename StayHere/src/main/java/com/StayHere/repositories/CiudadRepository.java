package com.StayHere.repositories;

import com.StayHere.entities.Ciudad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiudadRepository extends JpaRepository<Ciudad, Long> {
    Ciudad findBynombre(String nombre);
}
