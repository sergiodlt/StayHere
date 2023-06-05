package com.StayHere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.StayHere.entities.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{
	
}