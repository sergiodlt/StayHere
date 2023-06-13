package com.StayHere.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.StayHere.entities.Foto;



public interface FotoRepository extends JpaRepository<Foto, Long>{
    List<Foto> findByApartamentoId(Long apartamentoId);

	List<Foto> findByHabitacionId(Long habitacionId);

}
