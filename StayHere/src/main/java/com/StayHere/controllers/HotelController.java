package com.StayHere.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Foto;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.repositories.FotoRepository;
import com.StayHere.repositories.HotelRepository;
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

import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/hoteles")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HabitacionService habitacionService;

	@Autowired
	private ComodidadService comodidadService;

	@Autowired
	private CiudadService ciudadService;

	@Autowired
	private FotoRepository fotoRepository;

	@GetMapping("c")
	public String cGet(ModelMap m) {
		m.put("comodidades", comodidadService.getComodidades());
		List<Ciudad> ciudades = ciudadService.getCiudades();
		m.put("ciudades", ciudades);
		m.put("view", "hotel/c");
		return "_t/frame";
	}

	@GetMapping("prueba")
	public String prueba(ModelMap m) {
		m.put("view", "home/prueba");
		return "_t/frame";
	}

	@PostMapping("c")
	public String cPost(@RequestParam("nombre") String nombre, @RequestParam("direccion") String direccion,
			@RequestParam("telefono") int telefono, @RequestParam("correo") String correo,
			@RequestParam("estrellas") int estrellas, @RequestParam(required = false, name = "cantidad") int cantidad,
			/* @RequestParam("numero") int numero, */ @RequestParam("capacidad") int capacidad,
			@RequestParam("precio") int precio, @RequestParam("descripcion") String descripcion,@RequestParam("ciudad") Long idciudad,
			@RequestParam("fotos") List<MultipartFile> fotos,
			@RequestParam(required = false, name = "idComodidad") List<Long> idComodidades) throws DangerException {
		try {

			hotelService.saveHotel(nombre, direccion, telefono, correo, estrellas,idciudad);
			Hotel hotel = hotelService.obtenerUltimoHotelAgregado();

			for (int i = 1; i <= cantidad; i++) {
				habitacionService.saveHabitacion(descripcion, capacidad, precio, hotel.getId(), idComodidades);

				for (MultipartFile foto : fotos) {
					if (!foto.isEmpty()) {
						try {
							String nombreArchivo = foto.getOriginalFilename();

							// Guardar la imagen en la carpeta resources/static/img/
							Path rutaArchivo = Paths.get("src/main/resources/static/img/" + nombreArchivo);
							Files.write(rutaArchivo, foto.getBytes());
							Foto fotoBDD = new Foto();
							fotoBDD.setRuta("/img/" + nombreArchivo);
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

//	@PostMapping("/filtrados")
//	public String filtrador(ModelMap m, @RequestParam("idComodidades") List<Long> idComodidades) {
//
//		List<Comodidad> comodidadesFilt = new ArrayList<Comodidad>();
//		List<Hotel> hotelesFilt = new ArrayList<Hotel>();
//
//		for (Long idComodidad : idComodidades) {
//			Comodidad comodidad = comodidadService.getComodidadById(idComodidad);
//			comodidadesFilt.add(comodidad);
//		}
//
//		List<Comodidad> comodidades = comodidadService.getComodidades();
//		m.put("comodidades", comodidades);
//
//		List<Hotel> hoteles = hotelService.getHoteles();
//
//		m.put("hoteles", hotelesFilt);
//
//		m.put("view", "buscador");
//		return "_t/frame";
//	}
	/*
	 * @GetMapping("r") public String rGet(ModelMap m) { List<Hotel> hoteles =
	 * hotelService.getHoteles();
	 * 
	 * m.put("hoteles", hoteles); m.put("view", "hotel/r"); return "_t/frame"; }
	 * 
	 * @GetMapping("u") public String uGet(@RequestParam("id") Long idHotel,
	 * ModelMap m) { Hotel hotel = hotelService.getHotelById(idHotel);
	 * 
	 * m.put("ciudades", ciudadService.getCiudades()); m.put("hotel", hotel);
	 * m.put("view", "hotel/u");
	 * 
	 * return "_t/frame"; }
	 * 
	 * @PostMapping("u") public String uPost(
	 * 
	 * @RequestParam("idHotel") Long idHotel,
	 * 
	 * @RequestParam("nombre") String nombre,
	 * 
	 * @RequestParam("direccion") String direccion,
	 * 
	 * @RequestParam("telefono") int telefono,
	 * 
	 * @RequestParam("correo") String correo,
	 * 
	 * @RequestParam("descripcion") String descripcion,
	 * 
	 * @RequestParam("estrellas") int estrellas,
	 * 
	 * @RequestParam("precio") int precio,
	 * 
	 * @RequestParam(required=false, name="idCiudad") Long idCiudad ) throws
	 * DangerException { String retorno = "redirect:/hotel/r"; try {
	 * hotelService.updateHotel(idHotel, nombre, direccion, telefono, correo,
	 * descripcion, estrellas, precio, idCiudad); } catch (Exception e) {
	 * PRG.error(e.getMessage(), "/hotel/r"); } return retorno; }
	 * 
	 * @PostMapping("d") public String d(@RequestParam("id") Long id) {
	 * hotelService.deleteHotel(id); return "redirect:/hotel/r"; }
	 * 
	 * 
	 */

}

// ...
