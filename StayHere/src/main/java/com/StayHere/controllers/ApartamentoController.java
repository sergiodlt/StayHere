package com.StayHere.controllers;

import java.util.ArrayList;
import java.util.List;
import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.services.CiudadService;
import com.StayHere.services.ComodidadService;
import com.StayHere.services.ApartamentoService;
import com.StayHere.services.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/apartamento")
public class ApartamentoController {

	@Autowired
	private ApartamentoService apartamentoService;
	
	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private ComodidadService comodidadService;

	@GetMapping("c")
	public String cGet(ModelMap m) {
		m.put("ciudades", ciudadService.getCiudades());
		m.put("comodidades", comodidadService.getComodidades());
		m.put("view", "apartamento/c");
		return "_t/frame";
	}

	@PostMapping("c")
	public String cPost(
			@RequestParam("nombre") String nombre,
			@RequestParam("direccion") String direccion,
			@RequestParam("telefono") int telefono,
			@RequestParam("correo") String correo,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("precio") int precio,
			@RequestParam("capacidad") int capacidad,
			@RequestParam(required=false, name="idComodidad") List<Long> idComodidades,
			@RequestParam(required=false, name="idCiudad") Long idCiudad
			) throws DangerException {
		try {
			apartamentoService.saveApartamento(nombre, direccion, telefono, correo, descripcion, capacidad,precio, idCiudad, idComodidades);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/apartamento/r");
		}
		return "redirect:/apartamento/r";
	}

	@GetMapping("r")
	public String rGet(ModelMap m) {
		List<Apartamento> apartamentos = apartamentoService.getApartamentos();
		m.put("apartamentos", apartamentos);
		m.put("view", "apartamento/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(@RequestParam("id") Long idApartamento, ModelMap m) {
		Apartamento apartamento = apartamentoService.getApartamentoById(idApartamento);

		m.put("ciudad", ciudadService.getCiudades());
		m.put("apartamento", apartamento);
		m.put("view", "apartamento/u");

		return "_t/frame";
	}

	@PostMapping("u")
	public String uPost(
			@RequestParam("idApartamento") Long idApartamento,
			@RequestParam("nombre") String nombre,
			@RequestParam("direccion") String direccion,
			@RequestParam("telefono") int telefono,
			@RequestParam("correo") String correo,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("precio") int precio,
			@RequestParam(required=false, name="idCiudad") Long idCiudad
			
			) throws DangerException {
		String retorno = "redirect:/apartamento/r";
		try {
			apartamentoService.updateApartamento(idApartamento, nombre, direccion, telefono, correo, descripcion, precio, idCiudad);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/apartamento/r");
		}
		return retorno;
	}

	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		apartamentoService.deleteApartamento(id);
		return "redirect:/apartamento/r";
	}

}
