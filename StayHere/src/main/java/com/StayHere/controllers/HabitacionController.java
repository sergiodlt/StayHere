package com.StayHere.controllers;

import java.util.ArrayList;
import java.util.List;

import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.services.CiudadService;
import com.StayHere.services.ComodidadService;
import com.StayHere.services.HabitacionService;
import com.StayHere.services.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/habitacion")
public class HabitacionController {

	@Autowired
	private HabitacionService habitacionService;
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private ComodidadService comodidadService;

	@Autowired
	private CiudadService ciudadService;
	@GetMapping("c")
	public String cGet(ModelMap m) {
		m.put("hoteles", hotelService.getHoteles());
		m.put("view", "habitacion/c");
		return "_t/frame";
	}

	@PostMapping("c")
	public String cPost(
			@RequestParam("descripcion") String descripcion,
			@RequestParam("capacidad") int capacidad,
			@RequestParam("precio") int precio,
			@RequestParam(required=false, name="idHotel") Long idHotel
			
			) throws DangerException {
		try {
			//habitacionService.saveHabitacion(descripcion, capacidad, precio, idHotel);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/habitacion/r");
		}
		return "redirect:/habitacion/r";
	}
	@PostMapping("/filtrados")
	public String filtrador(ModelMap m, @RequestParam("idComodidades") List<Long> idComodidades) {

		List<Comodidad> comodidadesFilt = new ArrayList<Comodidad>();
		List<Habitacion> habitacionesFilt = new ArrayList<Habitacion>();

		for (Long idComodidad : idComodidades) {
			Comodidad comodidad = comodidadService.getComodidadById(idComodidad);
			comodidadesFilt.add(comodidad);
		}

		List<Comodidad> comodidades = comodidadService.getComodidades();
		m.put("comodidades", comodidades);

		List<Habitacion> habitaciones = habitacionService.getHabitaciones();
		for(Habitacion hab : habitaciones) {
			if(hab.getComodidades().containsAll(comodidadesFilt)) {
				habitacionesFilt.add(hab);
			}
		}
		

		m.put("habitaciones", habitacionesFilt);

		m.put("view", "buscador");
		return "_t/frame";
	}
	@GetMapping("/buscador")
	public String buscador(ModelMap m) {
		List<Ciudad> ciudades = ciudadService.getCiudades();
		m.put("ciudades", ciudades);

		List<Comodidad> comodidades = comodidadService.getComodidades();
		m.put("comodidades", comodidades);

		List<Habitacion> habitaciones = habitacionService.getHabitaciones();
		m.put("habitaciones", habitaciones);

		m.put("view", "buscador");
		return "_t/frame";

	}
	@GetMapping("r")
	public String rGet(ModelMap m) {
		List<Habitacion> habitaciones = habitacionService.getHabitaciones();
		m.put("habitaciones", habitaciones);
		m.put("view", "habitacion/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(@RequestParam("id") Long idHabitacion, ModelMap m) {
		Habitacion habitacion = habitacionService.getHabitacionById(idHabitacion);

		m.put("hoteles", hotelService.getHoteles());
		m.put("habitacion", habitacion);
		m.put("view", "habitacion/u");

		return "_t/frame";
	}

	@PostMapping("u")
	public String uPost(
			@RequestParam("idHabitacion") Long idHabitacion,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("capacidad") int capacidad,
			@RequestParam("precio") int precio,
			@RequestParam(required=false, name="idHotel") Long idHotel
			
			) throws DangerException {
		String retorno = "redirect:/habitacion/r";
		try {
			habitacionService.updateHabitacion(idHabitacion, descripcion, capacidad, precio,idHotel);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/habitacion/r");
		}
		return retorno;
	}

	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		habitacionService.deleteHabitacion(id);
		return "redirect:/habitacion/r";
	}

}

