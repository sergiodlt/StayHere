package com.StayHere.services;

import java.util.List;

import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.ComodidadRepository;
import com.StayHere.repositories.HabitacionRepository;
import com.StayHere.repositories.HotelRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComodidadService {

	@Autowired
	private ComodidadRepository comodidadRepository;
	
	@Autowired
	private HotelRepository hotelRepository;
	
	@Autowired
	private HabitacionRepository habitacionRepository;
	
	@Autowired
	private ApartamentoRepository apartamentoRepository;

	public List<Comodidad> getComodidades() {
		return comodidadRepository.findAll();
	}

	public void saveComodidad(String nombre,  Long idHabitacion, Long idApartamento) throws Exception {
		Comodidad comodidad = Comodidad.builder().nombre(nombre).build();
		
	
		/*Hotel hotel = hotelRepository.getById(idHotel);
		comodidad.setHotel(hotel);*/
		
	/*
		Habitacion habitacion = habitacionRepository.getById(idHabitacion);
		comodidad.setHabitacion(habitacion);
		
		Apartamento apartamento = apartamentoRepository.getById(idApartamento);
		comodidad.setApartamento(apartamento);*/
		
		try {
			comodidadRepository.saveAndFlush(comodidad);
		} catch (Exception e) {
			throw new Exception("El/la comodidad " + " ya existe");
		}
	}

	public Comodidad getComodidadById(Long id) {
		return comodidadRepository.findById(id).get();
	}

	public void updateComodidad(Long id, String nombre, Long idHotel, Long idHabitacion, Long idApartamento) throws Exception {
		Comodidad comodidad = comodidadRepository.findById(id).get();
		comodidad.setNombre(nombre);
		
		/*if (comodidad.getHotel() == null || idHotel != comodidad.getHotel().getId()) {
			Hotel nuevoHotelComodidad = hotelRepository.getById(idHotel);
			comodidad.setHotel(nuevoHotelComodidad);
			
		}*/
		
		/*if (comodidad.getHabitaciones() == null || idHabitacion != comodidad.getHabitaciones().getId()) {
			Habitacion nuevaHabitacionComodidad = habitacionRepository.getById(idHabitacion);
			comodidad.setHabitaciones(nuevaHabitacionComodidad);
			
		}*/
		
	/*	if (comodidad.getApartamento() == null || idApartamento != comodidad.getApartamento().getId()) {
			Apartamento nuevoApartamentoComodidad = apartamentoRepository.getById(idApartamento);
			comodidad.setApartamento(nuevoApartamentoComodidad);
			
		}*/
		
		try {
			comodidadRepository.saveAndFlush(comodidad);
		} catch (Exception e) {
			throw new Exception("El/la comodidad " +  " ya existe");
		}
	}

	public void deleteComodidad(Long id) {
		Comodidad comodidad = comodidadRepository.findById(id).get();
		comodidadRepository.delete(comodidad);
	}
}
