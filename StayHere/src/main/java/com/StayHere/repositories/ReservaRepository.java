package com.StayHere.repositories;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long>{

}
