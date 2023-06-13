package com.StayHere.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Foto;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.entities.User;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.FotoRepository;
import com.StayHere.repositories.UserRepository;
import com.StayHere.services.CiudadService;
import com.StayHere.services.ComodidadService;
import com.StayHere.services.ApartamentoService;
import com.StayHere.services.ReservaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/apartamento")
public class ApartamentoController {

	@Autowired
	private ApartamentoService apartamentoService;
	
	@Autowired
	private ApartamentoRepository apartamentoRepository;
	
	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private ComodidadService comodidadService;
	
	@Autowired
	private FotoRepository fotoRepository;
	
	@Autowired
	private UserRepository userRepository;

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
			@RequestParam(required=false, name="idCiudad") Long idCiudad,
			@RequestParam("fotos") List<MultipartFile> fotos
			) throws DangerException {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userRepository.findByUsername(username);
			apartamentoService.saveApartamento(nombre, direccion, telefono, correo, descripcion, capacidad,precio, idCiudad, idComodidades, user, fotos);
			
			
			
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/");
		}
		return "redirect:/";
	}

	@GetMapping("r")
	public String rGet(ModelMap m) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        
		m.put("apartamentos", user.getApartamentos());
		m.put("view", "apartamento/r");
		return "_t/frame";
	}
	@GetMapping("rAdmin")
	public String rAdmin(ModelMap m) {	
		m.put("apartamentos", apartamentoService.getApartamentos());
		m.put("view", "apartamento/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(@RequestParam("id") Long idApartamento, ModelMap m) {
		Apartamento apartamento = apartamentoService.getApartamentoById(idApartamento);

		m.put("ciudades", ciudadService.getCiudades());
		m.put("comodidades", comodidadService.getComodidades());
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
			@RequestParam("capacidad") int capacidad,
			@RequestParam(required=false, name="idComodidad") List<Long> idComodidades,
			@RequestParam(required=false, name="idCiudad") Long idCiudad,
			@RequestParam("fotos") List<MultipartFile> fotos
			
			) throws DangerException {
		String retorno = "redirect:/apartamento/r";
		try {
			apartamentoService.updateApartamento(idApartamento, nombre, direccion, telefono, correo,  descripcion, precio, idCiudad, capacidad, fotos);
		
		
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/apartamento/r");
		}
		return retorno;
	}

	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		apartamentoService.deleteApartamento(id);
		return "redirect:/";
	}
	
	@PostMapping("/verificar-apartamento-correo-tlf")
    public @ResponseBody Map<String, Boolean> verificarEmailExistente(@RequestParam("email") String email,@RequestParam("tlf") int tlf, @RequestParam("nombre") String nombre) {
        Boolean existEmail = apartamentoRepository.findByCorreo(email) != null;
        Boolean existTlf = apartamentoRepository.findByTelefono(tlf) != null;
        Boolean existNombre = apartamentoRepository.findByNombre(nombre) != null;
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("existTlf", existTlf);
        response.put("existEmail", existEmail);
        response.put("existNombre", existNombre);
        
        return response;
    }

}
