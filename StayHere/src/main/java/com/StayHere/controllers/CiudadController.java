package com.StayHere.controllers;

import java.util.List;
import com.StayHere.entities.Ciudad;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.services.CiudadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/ciudad")
public class CiudadController {

	@Autowired
	private CiudadService ciudadService;

	@GetMapping("c")
	public String cGet(ModelMap m) {
		m.put("view", "alojamiento/c");
		return "_t/frame";
	}

	@PostMapping("c")
	public String cPost(
			@RequestParam("nombre") String nombre
			) throws DangerException {
		try {
			ciudadService.saveCiudad(nombre);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/ciudad/r");
		}
		return "redirect:/ciudad/r";
	}

	@GetMapping("r")
	public String rGet(ModelMap m) {
		List<Ciudad> ciudades = ciudadService.getCiudades();
		m.put("ciudades", ciudades);
		m.put("view", "ciudades/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(
			@RequestParam("id") Long idCiudad, ModelMap m) {
		Ciudad ciudad = ciudadService.getCiudadById(idCiudad);

		m.put("ciudad", ciudad);
		m.put("view", "ciudad/u");

		return "_t/frame";
	}

	@PostMapping("u")
	public String uPost(@RequestParam("idCiudad") Long idCiudad,
			@RequestParam("nombre") String nombre) throws DangerException {
		String retorno = "redirect:/ciudad/r";
		try {
			ciudadService.updateCiudad(idCiudad, nombre);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/ciudad/r");
		}
		return retorno;
	}

	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		ciudadService.deleteCiudad(id);
		return "redirect:/ciudad/r";
	}
	


}
