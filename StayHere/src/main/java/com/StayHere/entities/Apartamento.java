package com.StayHere.entities;

import java.util.Collection;
import java.util.List;


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
public class Apartamento {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@NonNull
	private String nombre;
	
	private String direccion;
	
	private String correo;

	private int telefono;
	
	private String descripcion;
	
	private int precio;

	private int capacidad;
	
	@ManyToMany
    private List<Comodidad> comodidades;
	
	@ManyToOne
	private Ciudad ciudad;
	
	@OneToMany(mappedBy = "apartamento")
	private Collection<Reserva> reservas;
	
	@OneToMany(mappedBy = "apartamento")
	private Collection<Foto> fotos;
}
