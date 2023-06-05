package com.StayHere.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
public class Reserva {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate fecha_inicio;
	
    private LocalDate fecha_fin;
	 
	private int huespedes;
	 
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Habitacion habitacion;
	
	@ManyToOne
	private Apartamento apartamento;
	
}
