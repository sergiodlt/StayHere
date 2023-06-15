package com.StayHere.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.StayHere.entities.Habitacion;


public interface HabitacionRepository extends JpaRepository<Habitacion, Long>{
	Optional<Habitacion> findFirstByOrderByIdDesc();
	   
	@Query("SELECT h FROM Habitacion h WHERE NOT EXISTS " +
		       "(SELECT r FROM h.reservas r WHERE (r.fecha_inicio <= :checkout AND r.fecha_fin >= :checkin)) " +
		       "AND (h.capacidad IS NULL OR h.capacidad >= :numHuespedes)")
		List<Habitacion> findHabitacionesDisponibles(
		    @Param("checkin") LocalDate checkin,
		    @Param("checkout") LocalDate checkout,
		    @Param("numHuespedes") int numHuespedes);

	@Query("SELECT COUNT(r) FROM Habitacion h LEFT JOIN h.reservas r " +
		       "WHERE (r.fecha_inicio <= :checkout AND r.fecha_fin >= :checkin) " +
		       "AND h.id = :idHabitacion")
		int countHabitacionesDisponiblesAjax(
		    @Param("checkin") LocalDate checkin,
		    @Param("checkout") LocalDate checkout,
		    @Param("idHabitacion") Long idHabitacion);




	   
	
}
