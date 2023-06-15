package com.StayHere.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Foto;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.User;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.repositories.FotoRepository;
import com.StayHere.repositories.ReservaRepository;
import com.StayHere.repositories.UserRepository;
import com.StayHere.services.CiudadService;
import com.StayHere.services.ComodidadService;
import com.StayHere.services.HabitacionService;
import com.StayHere.services.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

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
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FotoRepository fotoRepository;

	@Autowired
	private ReservaRepository reservaRepository;
	
	@GetMapping("c")
	public String cGet(@RequestParam("id") Long idHotel,ModelMap m) {
		
		m.put("hotel", hotelService.getHotelById(idHotel));
		m.put("comodidades", comodidadService.getComodidades());
		List<Ciudad> ciudades = ciudadService.getCiudades();
		m.put("ciudades", ciudades);
		m.put("view", "habitacion/c");
		return "_t/frame";
	}

	@PostMapping("cPost")
	public String cPost( @RequestParam(required = false, name = "cantidad") int cantidad,
			@RequestParam("capacidad") int capacidad,@RequestParam("precio") int precio, @RequestParam("descripcion") String descripcion,
			@RequestParam("id") Long idHotel, @RequestParam(required = false, name = "fotos") List<MultipartFile> fotos, @RequestParam(required = false, name = "idComodidad") List<Long> idComodidades) throws DangerException {
		try {

			for (int i = 1; i <= cantidad; i++) {
				habitacionService.saveHabitacion(descripcion, capacidad, precio, idHotel, idComodidades);

				for (MultipartFile foto : fotos) {
				    if (!foto.isEmpty()) {
				        try {
				            String nombreArchivo = foto.getOriginalFilename();

				            // Guardar la imagen en la carpeta resources/static/img/
				            Path rutaArchivo = Paths.get("images/" + nombreArchivo);
				            Files.write(rutaArchivo, foto.getBytes());
				            Foto fotoBDD = new Foto();
				            fotoBDD.setRuta("/images/" + nombreArchivo);
				            fotoBDD.setHabitacion(habitacionService.obtenerUltimaHabitacionAgregada());
				            fotoRepository.save(fotoBDD);

				        } catch (IOException e) {
				            e.printStackTrace();
				        }
				    }
				}

			}
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/hotel/error");
		}
		return "redirect:/";
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
	public String rGet(@RequestParam("id") Long id, ModelMap m) {
		Hotel hotel= hotelService.getHotelById(id);
		Collection<Habitacion> habitaciones = hotel.getHabitaciones();
		m.put("habitaciones", habitaciones);
		m.put("hotel", hotel);
		m.put("view", "habitacion/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(@RequestParam("id") Long idHabitacion, ModelMap m) {
		Habitacion habitacion = habitacionService.getHabitacionById(idHabitacion);

		m.put("comodidades", comodidadService.getComodidades());
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
	        @RequestPart(required = false, name = "fotos") List<MultipartFile> fotos
	) throws DangerException {
	    String retorno = "redirect:/";
	    try {
	        habitacionService.updateHabitacion(idHabitacion, descripcion, capacidad, precio, fotos);
	    } catch (Exception e) {
	        PRG.error(e.getMessage(), "/");
	    }
	    return retorno;
	}

	@PostMapping("uAdmin")
	public String uPostAdmin(
			@RequestParam("idHabitacion") Long idHabitacion,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("capacidad") int capacidad,
			@RequestParam("precio") int precio,
			@RequestPart(required = false, name = "fotos") List<MultipartFile> fotos
			) throws DangerException {
		String retorno = "redirect:/habitacion/r?id="+idHabitacion;
		try {
			habitacionService.updateHabitacion(idHabitacion, descripcion, capacidad, precio, fotos);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/");
		}
		return retorno;
	}


	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		Habitacion habitacion = habitacionService.getHabitacionById(id);
		for (Foto foto : habitacion.getFotos()) {
			fotoRepository.delete(foto);
		}

		for (Reserva reserva : habitacion.getReservas()) {
			reservaRepository.delete(reserva);
		}
		habitacionService.deleteHabitacion(id);
		return "redirect:/";
	}

}

