package com.StayHere.services;

import java.time.LocalDate;
import java.util.List;

import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.User;
import com.StayHere.repositories.CiudadRepository;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.HotelRepository;
import com.StayHere.repositories.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

	@Autowired
	private ReservaRepository reservaRepository;
	
	@Autowired
	private UserService userService;

	@Autowired
	private HabitacionService habitacionService;

	@Autowired
	private ApartamentoService apartamentoService;
	

	public List<Reserva> getReservas() {
		return reservaRepository.findAll();
	}

	public void saveReservaHotel(LocalDate fecha_inicio,LocalDate fecha_fin, User user, Long idHabitacion, int huespedes, int precio) throws Exception {
		Reserva reserva = Reserva.builder().fecha_inicio(fecha_inicio).fecha_fin(fecha_fin).huespedes(huespedes).user(user).build();
		
	
		/*
		Apartamento apartamento = apartamentoRepository.getById(idApartamento);
		reserva.setApartamento(apartamento);
		*/
		Habitacion habitacion = habitacionService.getHabitacionById(idHabitacion);
		reserva.setHabitacion(habitacion);
		
		
		try {
			reservaRepository.saveAndFlush(reserva);
		} catch (Exception e) {
			throw new Exception("El/la reserva de esta habitacion ya existe");
		}
	}
	
	public void saveReservaApartamento(LocalDate fecha_inicio,LocalDate fecha_fin, User user, Long idApartamento, int huespedes, int precio) throws Exception {
		Reserva reserva = Reserva.builder().fecha_inicio(fecha_inicio).fecha_fin(fecha_fin).huespedes(huespedes).user(user).build();
		
	
		/*
		Apartamento apartamento = apartamentoRepository.getById(idApartamento);
		reserva.setApartamento(apartamento);
		*/
		Apartamento apartamento = apartamentoService.getApartamentoById(idApartamento);
		reserva.setApartamento(apartamento);
		
		
		try {
			reservaRepository.saveAndFlush(reserva);
		} catch (Exception e) {
			throw new Exception("El/la reserva de esta apartamento ya existe");
		}
	}

	public Reserva getReservaById(Long id) {
		return reservaRepository.findById(id).get();
	}

	/*public void updateReserva(Long id, LocalDate fecha_inicio,LocalDate fecha_fin, Long idUsuario, Long idHotel, Long idApartamento, int huespedes, int precio)  throws Exception {
		Reserva reserva = reservaRepository.findById(id).get();
		reserva.setFecha_inicio(fecha_inicio);
		reserva.setFecha_fin(fecha_fin);
		reserva.setHuespedes(huespedes);
		reserva.setPrecio(precio);
		
		if (reserva.getHotel()== null || idHotel != reserva.getHotel().getId()) {
			Hotel hotel = hotelRepository.getById(idHotel);
			reserva.setHotel(hotel);
		}
		
		if (reserva.getApartamento()== null || idApartamento != reserva.getApartamento().getId()) {
			Apartamento  apartamento = apartamentoRepository.getById(idApartamento);
			reserva.setApartamento (apartamento );
		}
		
		
		try {
			reservaRepository.saveAndFlush(reserva);
		} catch (Exception e) {
			throw new Exception("La reserva ya existe");
		}
	}*/

	public void deleteReserva(Long id) {
		Reserva reserva = reservaRepository.findById(id).get();
		reservaRepository.delete(reserva);
	}
}
