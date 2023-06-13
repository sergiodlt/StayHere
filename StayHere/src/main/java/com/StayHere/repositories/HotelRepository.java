package com.StayHere.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayHere.entities.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findFirstByOrderByIdDesc();

	Hotel findByCorreo(String email);

	Hotel findByTelefono(String tlf);

	Hotel findByNombre(String nombre);
}

