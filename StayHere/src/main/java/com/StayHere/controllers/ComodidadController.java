package com.StayHere.controllers;

import java.util.List;
import com.StayHere.entities.Comodidad;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.services.ApartamentoService;
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
@RequestMapping("/comodidades")
public class ComodidadController {

	@Autowired
	private ComodidadService comodidadService;
	
	@Autowired
	private HotelService hotelService;
	
	@Autowired
	private HabitacionService habitacionService;
	
	@Autowired
	private ApartamentoService apartamentoService;

	@GetMapping("c")
	public String cGet(ModelMap m) {
		m.put("hoteles", hotelService.getHoteles());
		m.put("habitaciones", habitacionService.getHabitaciones());
		m.put("apartamentos", apartamentoService.getApartamentos());
		m.put("view", "comodidad/c");
		return "_t/frame";
	}

	@PostMapping("c")
	public String cPost(
			@RequestParam("nombre") String nombre,
			@RequestParam(required=false, name="idHabitacion") Long idHabitacion,
			@RequestParam(required=false, name="idApartamento") Long idApartamento
			) throws DangerException {
		try {
			comodidadService.saveComodidad(nombre, idHabitacion, idApartamento);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/");
		}
		return "redirect:/comodidad/r";
	}

	@GetMapping("r")
	public String rGet(ModelMap m) {
		List<Comodidad> comodidades = comodidadService.getComodidades();
		m.put("comodidades", comodidades);
		m.put("view", "comodidad/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(@RequestParam("id") Long idComodidad, ModelMap m) {
		Comodidad comodidad = comodidadService.getComodidadById(idComodidad);

		m.put("hoteles", hotelService.getHoteles());
		m.put("habitaciones", habitacionService.getHabitaciones());
		m.put("apartamentos", apartamentoService.getApartamentos());
		m.put("comodidad", comodidad);
		m.put("view", "comodidad/u");

		return "_t/frame";
	}

	@PostMapping("u")
	public String uPost(
			@RequestParam("idComodidad") Long idComodidad,
			@RequestParam("nombre") String nombre,
			@RequestParam(required=false, name="idHotel") Long idHotel,
			@RequestParam(required=false, name="idHabitacion") Long idHabitacion,
			@RequestParam(required=false, name="idApartamento") Long idApartamento
			) throws DangerException {
		String retorno = "redirect:/comodidad/r";
		try {
			comodidadService.updateComodidad(idComodidad,nombre, idHotel, idHabitacion,idApartamento);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/comodidad/r");
		}
		return retorno;
	}

	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		comodidadService.deleteComodidad(id);
		return "redirect:/comodidad/r";
	}

}

