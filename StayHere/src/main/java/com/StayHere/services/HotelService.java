package com.StayHere.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.User;
import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Foto;
import com.StayHere.entities.Habitacion;
import com.StayHere.repositories.CiudadRepository;
import com.StayHere.repositories.ComodidadRepository;
import com.StayHere.repositories.FotoRepository;
import com.StayHere.repositories.HabitacionRepository;
import com.StayHere.repositories.HotelRepository;
import com.StayHere.repositories.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
public class HotelService {

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private HabitacionRepository habitacionRepository;

	@Autowired
	private CiudadRepository ciudadRepository;

	@Autowired
	private ComodidadRepository comodidadRepository;

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private FotoRepository fotoRepository;

	public List<Hotel> getHoteles() {

		List<Hotel> hoteles = new ArrayList<>();
		hoteles.addAll(hotelRepository.findAll());
		return hoteles;
	}

	public Hotel obtenerUltimoHotelAgregado() {
		Optional<Hotel> ultimoHotel = hotelRepository.findFirstByOrderByIdDesc();
		return ultimoHotel.orElse(null);
	}

	public void saveHotel(String nombre, String direccion, int telefono, String correo, int estrellas, Long idCiudad,
			User user) throws Exception {
		Ciudad ciudad = ciudadRepository.getById(idCiudad);
		Hotel hotel = Hotel.builder().nombre(nombre).direccion(direccion).telefono(telefono).correo(correo)
				.estrellas(estrellas).ciudad(ciudad).user(user).build();

		try {
			hotelRepository.saveAndFlush(hotel);
		} catch (Exception e) {
			throw new Exception("El/la hotel " + nombre + " ya existe");
		}
	}

	public Hotel getHotelById(Long id) {
		return hotelRepository.findById(id).get();
	}

	public void updateHotel(Long id, String nombre, String direccion, int telefono, String correo, String descripcion,
			int estrellas, Ciudad ciudad) throws Exception {
		Hotel hotel = hotelRepository.findById(id).get();

		hotel.setCiudad(ciudad);
		hotel.setCorreo(correo);
		hotel.setNombre(nombre);
		hotel.setDireccion(direccion);
		hotel.setTelefono(telefono);
		hotel.setEstrellas(estrellas);

		try {
			hotelRepository.saveAndFlush(hotel);
		} catch (Exception e) {
			throw new Exception("El/la hotel " + nombre + " ya existe");
		}
	}

	public void deleteHotel(Long id) {
		Hotel hotel = hotelRepository.findById(id).get();
		Collection<Habitacion> habitaciones = hotel.getHabitaciones();
		for (Habitacion hab : habitaciones) {

			for (Foto foto : hab.getFotos()) {
				fotoRepository.delete(foto);
			}

			for (Reserva reserva : hab.getReservas()) {
				reservaRepository.delete(reserva);
			}

			habitacionRepository.delete(hab);
		}

		hotelRepository.delete(hotel);
	}

	/*
	 * public List<Hotel> filtrarPorComodidades(List<Long> comodidadesIds) { return
	 * hotelRepository.findByComodidadesIdIn(comodidadesIds); }
	 */
}
