package com.StayHere.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Hotel;

public interface ApartamentoRepository extends JpaRepository<Apartamento, Long>{
	
	 @Query("SELECT a FROM Apartamento a LEFT JOIN a.reservas r " +
		       "WHERE (r IS NULL AND (a.capacidad IS NULL OR a.capacidad >= :numHuespedes)) " +
		       "OR ((r.fecha_inicio > :checkout OR r.fecha_fin < :checkin) " +
		       "AND (a.capacidad IS NULL OR a.capacidad >= :numHuespedes))")
		List<Apartamento> findApartamentosDisponibles(
		    @Param("checkin") LocalDate checkin,
		    @Param("checkout") LocalDate checkout,
		    @Param("numHuespedes") int numHuespedes); 
	 
	 @Query("SELECT COUNT(a) FROM Apartamento a LEFT JOIN a.reservas r " +
		       "WHERE (r IS NULL OR (r.fecha_inicio > :checkout OR r.fecha_fin < :checkin)) " +
		       "AND a.id = :idApartamento")
		int countApartamentosDisponiblesAjax(
		    @Param("checkin") LocalDate checkin,
		    @Param("checkout") LocalDate checkout,
		    @Param("idApartamento") Long idHabitacion);

	 
	    Optional<Apartamento> findFirstByOrderByIdDesc();




		Apartamento findByCorreo(String email);




		Apartamento findByTelefono(int tlf);




		Apartamento findByNombre(String nombre);

}