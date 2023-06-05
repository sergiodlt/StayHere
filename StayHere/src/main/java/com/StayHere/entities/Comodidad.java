package com.StayHere.entities;


import java.util.Collection;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


@Entity
public class Comodidad {

	public Comodidad(int i, String string) {
		// TODO Auto-generated constructor stub
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String nombre;
	
	@ManyToMany(mappedBy = "comodidades")
	private Collection<Habitacion> habitaciones;
		
	@ManyToMany(mappedBy = "comodidades")
	private Collection<Apartamento> apartamento;

	private String Icono;
		
	
}

