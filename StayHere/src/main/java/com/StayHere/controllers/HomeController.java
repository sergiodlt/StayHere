package com.StayHere.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.CiudadRepository;
import com.StayHere.repositories.HabitacionRepository;
import com.StayHere.repositories.HotelRepository;
import com.StayHere.services.CiudadService;
import com.StayHere.services.ComodidadService;
import com.StayHere.services.HotelService;

@Controller
public class HomeController {

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private ComodidadService comodidadService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HabitacionRepository habitacionRepository;

	@Autowired
	private ApartamentoRepository apartamentoRepository;

	@GetMapping("/")
	public String home(ModelMap m) {
		List<Ciudad> ciudades = ciudadService.getCiudades();
		m.put("ciudades", ciudades);

		m.put("view", "home");
		return "_t/frame";
	}

	@PostMapping("/home/buscador")
	public String buscador(@RequestParam("alojamiento") String alojamiento, @RequestParam("checkIn") LocalDate checkIn, @RequestParam("checkOut") LocalDate checkOut,
			@RequestParam("huespedes") int numHuespedes, @RequestParam("IdCiudad") Long ciudad, ModelMap m) {

		if(alojamiento.equals("hotel")) {
		List<Habitacion> habitaciones = habitacionRepository.findHabitacionesDisponibles(checkIn, checkOut, numHuespedes);
		List<Habitacion> habitacionesFilt = new ArrayList<Habitacion>();
		
		for(Habitacion hab : habitaciones) {
			if(hab.getHotel().getCiudad().getId() == ciudad) {
				habitacionesFilt.add(hab);
			}
		}
		m.put("alojamientos", habitacionesFilt);
		
	}
		if(alojamiento.equals("apartamento")) {
		List<Apartamento> apartamentos = apartamentoRepository.findApartamentosDisponibles(checkIn, checkOut, numHuespedes);
		List<Apartamento> apartamentosFilt = new ArrayList<Apartamento>();
		
			for(Apartamento apart : apartamentos) {
				if(apart.getCiudad().getId() == ciudad) {
					apartamentosFilt.add(apart);
				}
			}
			m.put("alojamientos", apartamentosFilt);
		}
		List<Comodidad> comodidades = comodidadService.getComodidades();
		m.put("alojamient", alojamiento);
		m.put("ciudades", ciudadService.getCiudades());
		m.put("checkin", checkIn);
		m.put("checkout", checkOut);
		m.put("capacidad", numHuespedes);
		m.put("ciudad", ciudadService.getCiudadById(ciudad));
		m.put("comodidades", comodidades);
		m.put("view", "buscador");
		return "_t/frame";
	}
}
