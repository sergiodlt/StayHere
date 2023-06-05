package com.StayHere.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Habitacion;
import com.StayHere.repositories.CiudadRepository;
import com.StayHere.repositories.ComodidadRepository;
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
	private CiudadRepository ciudadRepository;

	@Autowired
	private ComodidadRepository comodidadRepository;

	public  List<Hotel> getHoteles() {
		
		List<Hotel> hoteles = new ArrayList<>();
		hoteles.addAll(hotelRepository.findAll());
		return hoteles;
	}
	public Hotel obtenerUltimoHotelAgregado() {
        Optional<Hotel> ultimoHotel = hotelRepository.findFirstByOrderByIdDesc();
        return ultimoHotel.orElse(null);
    }

	public void saveHotel(String nombre,String direccion, int telefono,String correo, int estrellas, Long idCiudad) throws Exception {
		Ciudad ciudad = ciudadRepository.getById(idCiudad);
		Hotel hotel = Hotel.builder().nombre(nombre).direccion(direccion).telefono(telefono).correo(correo).estrellas(estrellas).ciudad(ciudad).build();
		
		/*Comodidad comodidad= comodidadRepository.getById(idComodidad);
		Collection<Comodidad>comodidades=new ArrayList<Comodidad>();
comodidades.add(comodidad);*/
		

		//udad ciudad = ciudadRepository.getById(idCiudad);
		//hotel.setCiudad(ciudad);
		// Gestión de aficiones
				/*Collection<Aficion>aficionGustos=new ArrayList<Aficion>();
				for ( Long idGusto : idGustos) {
					Aficion aficionGustada = aficionRepository.getById(idGusto);
					//persona.getGustos().add(aficionGustada);
					//aficionGustada.getAficionados().add(persona);
					aficionGustos.add(aficionGustada);
				}
				persona.setGustos(aficionGustos);
				
				Collection<Aficion>aficionOdios=new ArrayList<Aficion>();
				for ( Long idOdio : idOdios) {
					Aficion aficionOdiada = aficionRepository.getById(idOdio);
					//persona.getOdios().add(aficionOdiada);
					//aficionOdiada.getHaters().add(persona);
					aficionOdios.add(aficionOdiada);
				}
				persona.setOdios(aficionOdios);
				*/
		
		
		
		
		
		
		try {
			hotelRepository.saveAndFlush(hotel);
		} catch (Exception e) {
			throw new Exception("El/la hotel " + nombre + " ya existe");
		}
	}

	public Hotel getHotelById(Long id) {
		return hotelRepository.findById(id).get();
	}

/*	public void updateHotel(Long id, String nombre,String direccion, int telefono,String correo, String descripcion,int estrellas, int precio, Long idComodidad) throws Exception {
		Hotel hotel = hotelRepository.findById(id).get();
		
		hotel.setNombre(nombre);
		hotel.setDireccion(direccion);
		hotel.setTelefono(telefono);
		hotel.setCorreo(correo);
		hotel.setDescripcion(descripcion);
		hotel.setPrecio(precio);
		
		if (hotel.getCiudad() == null || idCiudad != hotel.getCiudad().getId()) {
			Ciudad nuevoAlojamientoHotel = ciudadRepository.getById(idCiudad);
			hotel.setCiudad(nuevoAlojamientoHotel);
			
		}
		try {
			hotelRepository.saveAndFlush(hotel);
		} catch (Exception e) {
			throw new Exception("El/la hotel " + nombre + " ya existe");
		}
	}*/

	public void deleteHotel(Long id) {
		Hotel hotel = hotelRepository.findById(id).get();
		hotelRepository.delete(hotel);
	}
	
	/* public List<Hotel> filtrarPorComodidades(List<Long> comodidadesIds) {
	        return hotelRepository.findByComodidadesIdIn(comodidadesIds);
	    }*/
}
