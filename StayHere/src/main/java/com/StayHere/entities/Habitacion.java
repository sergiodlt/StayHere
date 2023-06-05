package com.StayHere.entities;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
public class Habitacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String descripcion;
	
	private int capacidad;
	
	private int precio;
		
	@ManyToOne
	private Hotel hotel;

	@ManyToMany
	private Collection<Comodidad> comodidades;
	
	@OneToMany(mappedBy = "habitacion")
	private Collection<Foto> fotos;
	
	@OneToMany(mappedBy = "habitacion")
	private Collection<Reserva> reservas;

	
	

}
