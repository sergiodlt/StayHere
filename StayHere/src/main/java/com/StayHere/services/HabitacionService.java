package com.StayHere.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.repositories.ComodidadRepository;
import com.StayHere.repositories.HabitacionRepository;
import com.StayHere.repositories.HotelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HabitacionService {

	@Autowired
	private HabitacionRepository habitacionRepository;
	
	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private ComodidadService comodidadService;
	

	public List<Habitacion> getHabitaciones() {
		return habitacionRepository.findAll();
	}
	public Habitacion obtenerUltimaHabitacionAgregada() {
        Optional<Habitacion> ultimaHabitacion = habitacionRepository.findFirstByOrderByIdDesc();
        return ultimaHabitacion.orElse(null);
    }

	public void saveHabitacion(String descripcion, int capacidad, int precio, Long idHotel, List<Long> idComodidades) throws Exception {
		Habitacion habitacion = Habitacion.builder().descripcion(descripcion).capacidad(capacidad).precio(precio).build();
		List<Comodidad> comodidades = new ArrayList<Comodidad>();
		for(Long idComodidad : idComodidades) {
			Comodidad comod= comodidadService.getComodidadById(idComodidad);
			comodidades.add(comod);
		}
		habitacion.setComodidades(comodidades);
		
		Hotel hotel = hotelRepository.getById(idHotel);
		habitacion.setHotel(hotel);
		
		try {
			habitacionRepository.saveAndFlush(habitacion);
		} catch (Exception e) {
			throw new Exception("El/la habitacion " + " ya existe");
		}
	}

	public Habitacion getHabitacionById(Long id) {
		return habitacionRepository.findById(id).get();
	}

	public void updateHabitacion(Long id, String descripcion, int capacidad, int precio, Long idHotel) throws Exception {
		Habitacion habitacion = habitacionRepository.findById(id).get();
		habitacion.setDescripcion(descripcion);
		habitacion.setCapacidad(capacidad);
		habitacion.setPrecio(precio);
		
				if (habitacion.getHotel() == null || idHotel != habitacion.getHotel().getId()) {
					Hotel nuevoHotelHabitacion = hotelRepository.getById(idHotel);
					habitacion.setHotel(nuevoHotelHabitacion);
		}
				
		try {
			habitacionRepository.saveAndFlush(habitacion);
		} catch (Exception e) {
			throw new Exception("El/la habitacion " + " ya existe");
		}
	}

	public void deleteHabitacion(Long id) {
		Habitacion habitacion = habitacionRepository.findById(id).get();
		habitacionRepository.delete(habitacion);
	}
	/* public List<Habitacion> searchAvailableHotels(String city, LocalDate checkIn, LocalDate checkOut, int numGuests) {
	        return hotelRepository.findAvailableHotelsByCityAndGuestsAndDates(city, checkIn, checkOut, numGuests);
	    }*/
}

