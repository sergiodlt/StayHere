package com.StayHere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.StayHere.entities.Comodidad;
import com.StayHere.entities.User;


public interface ComodidadRepository extends JpaRepository<Comodidad, Long>{
	
	Comodidad findBynombre(String nombre);
}
