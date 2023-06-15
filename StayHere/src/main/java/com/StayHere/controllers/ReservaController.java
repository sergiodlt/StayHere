package com.StayHere.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Habitacion;
import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.User;
import com.StayHere.exception.DangerException;
import com.StayHere.helpers.PRG;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.HabitacionRepository;
import com.StayHere.repositories.ReservaRepository;
import com.StayHere.repositories.UserRepository;
import com.StayHere.services.ApartamentoService;
import com.StayHere.services.EmailService;
import com.StayHere.services.HabitacionService;
import com.StayHere.services.HotelService;
import com.StayHere.services.ReservaService;

@Controller
public class ReservaController {

	@Autowired
	private ReservaService reservaService;

	@Autowired
	private ReservaRepository reservaRepository;

	@Autowired
	private ApartamentoService apartamentoService;

	@Autowired
	private ApartamentoRepository apartamentoRepository;

	@Autowired
	private HabitacionService habitacionService;

	@Autowired
	private HabitacionRepository habitacionRepository;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

	@GetMapping("/reserva/r")
	public String r(ModelMap m) {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(username);
		List<Reserva> reservas = reservaService.getReservas();
		List<Reserva> reservasUser = new ArrayList<Reserva>();
		for (Reserva reserv : reservas) {
			if (reserv.getUser().getId() == user.getId()) {
				reservasUser.add(reserv);
			}
		}

		m.put("reservas", reservasUser);
		m.put("view", "reserva/r");
		return "_t/frame";
	}

	@GetMapping("/reserva/{id}")
	public String reserva(@PathVariable("id") Long id, @RequestParam("alojamiento") String alojamient,
			@RequestParam("checkin") LocalDate checkIn, @RequestParam("checkout") LocalDate checkOut,
			@RequestParam("capacidad") int capacidad, ModelMap m) {

		// Obtener los datos necesarios del alojamiento
		if (alojamient.equals("hotel")) {
			Habitacion alojamiento = habitacionService.getHabitacionById(id);
			m.put("alojamiento", alojamiento);

			int precioTotal = alojamiento.getPrecio() * (int) ChronoUnit.DAYS.between(checkIn, checkOut);
			m.put("precioTotal", precioTotal);
		} else if (alojamient.equals("apartamento")) {
			Apartamento alojamiento = apartamentoService.getApartamentoById(id);
			m.put("alojamiento", alojamiento);

			int precioTotal = alojamiento.getPrecio() * (int) ChronoUnit.DAYS.between(checkIn, checkOut);
			m.put("precioTotal", precioTotal);
		}

		// Agregar los demás datos al ModelMap
		m.put("alojamient", alojamient);
		m.put("capacidad", capacidad);
		m.put("checkin", checkIn);
		m.put("checkout", checkOut);
		m.put("view", "reserva/reserva");
		return "_t/frame";
	}

	@PostMapping("/reservaPost")
	public String cPost(@RequestParam(required = false, name = "fecha_inicio") LocalDate fecha_inicio,
			@RequestParam(required = false, name = "fecha_fin") LocalDate fecha_fin,
			@RequestParam(required = false, name = "huespedes") int huespedes,
			@RequestParam(required = false, name = "precio") int precio,
			@RequestParam(required = false, name = "alojamiento") String alojamiento,
			@RequestParam(required = false, name = "idAlojamiento") Long idAlojamiento) throws DangerException {
		try {
			String username = SecurityContextHolder.getContext().getAuthentication().getName();
			User user = userRepository.findByUsername(username);
			if (alojamiento.equals("hotel")) {
				reservaService.saveReservaHotel(fecha_inicio, fecha_fin, user, idAlojamiento, huespedes, precio);

			}
			if (alojamiento.equals("apartamento")) {
				reservaService.saveReservaApartamento(fecha_inicio, fecha_fin, user, idAlojamiento, huespedes, precio);
			}

		} catch (Exception e) {
			PRG.error(e.getMessage(), "/reserva/r");
		}
		return "redirect:/";
	}

	@GetMapping("/reservasAdmin/r")
	public String rGet(ModelMap m) {
		List<Reserva> reservas = reservaService.getReservas();
		m.put("reservas", reservas);
		m.put("view", "reserva/r");
		return "_t/frame";
	}

	@GetMapping("u")
	public String uGet(@RequestParam("id") Long idReserva, ModelMap m) {
		Reserva reserva = reservaService.getReservaById(idReserva);

		m.put("reserva", reserva);
		m.put("view", "reserva/u");

		return "_t/frame";
	}

	@PostMapping("reserva/d")
	public String delete(@RequestParam("id") Long idReserva, ModelMap m) {
		reservaService.deleteReserva(idReserva);

		return "redirect:/reserva/r";
	}
	@PostMapping("reserva/dAdmin")
	public String deleteReservAdmin(@RequestParam("id") Long idReserva, ModelMap m) {
		reservaService.deleteReserva(idReserva);
		
		return "redirect:/reservasAdmin/r";
	}

	/*
	 * @PostMapping("u") public String uPost(
	 * 
	 * @RequestParam("idReserva") Long idReserva,
	 * 
	 * @RequestParam("fecha_inicio") LocalDate fecha_inicio,
	 * 
	 * @RequestParam("fecha_fin") LocalDate fecha_fin,
	 * 
	 * @RequestParam("habitaciones") int habitaciones,
	 * 
	 * @RequestParam("huespedes") int huespedes,
	 * 
	 * @RequestParam("precio") int precio,
	 * 
	 * @RequestParam(required=false, name="idUsuario") Long idUsuario,
	 * 
	 * @RequestParam(required=false, name="idHotel") Long idHotel,
	 * 
	 * @RequestParam(required=false, name="idApartamento") Long idApartamento )
	 * throws DangerException { String retorno = "redirect:/reserva/r"; try {
	 * reservaService.updateReserva(idReserva, fecha_inicio, fecha_fin, idUsuario,
	 * idHotel, idApartamento, huespedes, precio); } catch (Exception e) {
	 * PRG.error(e.getMessage(), "/reserva/r"); } return retorno; }
	 */

	@PostMapping("d")
	public String d(@RequestParam("id") Long id) {
		reservaService.deleteReserva(id);
		return "redirect:/reserva/r";
	}

	@PostMapping("/verificar-reserva")
	public @ResponseBody Boolean verificarEmailExistente(@RequestParam("fechaInicio") LocalDate fechaInicio,
			@RequestParam("fechaFin") LocalDate fechaFin, @RequestParam("alojamiento") String alojamiento,
			@RequestParam("idAlojamiento") Long idAlojamiento) {

		boolean isAvailable = true;

		if (alojamiento.equals("hotel")) {
			boolean habitacionesDisponibles = (habitacionRepository.countHabitacionesDisponiblesAjax(fechaInicio,
					fechaFin, idAlojamiento) > 0);
			if (habitacionesDisponibles) {
				isAvailable = false;
			}
		}else {
				boolean apartamentosDisponibles = (apartamentoRepository.countApartamentosDisponiblesAjax(fechaInicio,
						fechaFin, idAlojamiento) > 0);
				if (apartamentosDisponibles) {
					isAvailable = false;
				}
			}
		
		return isAvailable;
	}
//	    if (alojamiento.equals("hotel")) {
//	        for (Reserva reserva : reservaService.getReservas()) {
//	            Habitacion habitacion = reserva.getHabitacion();
//	            if (habitacion != null && habitacion.getId() == idAlojamiento) {
//	                LocalDate reservaFechaInicio = reserva.getFecha_inicio();
//	                LocalDate reservaFechaFin = reserva.getFecha_fin();
//
//	                // Check if the given dates overlap with any existing reservation
//	                if (!(fechaFin.isBefore(reservaFechaInicio) || fechaIncio.isAfter(reservaFechaFin))) {
//	                    isAvailable = false;
//	                    break; // Stop checking further reservations
//	                }
//	            }
//	        }
//	    } else {
//	        for (Reserva reserva : reservaService.getReservas()) {
//	            Apartamento apartamento = reserva.getApartamento();
//	            if (apartamento != null && apartamento.getId() == idAlojamiento) {
//	                LocalDate reservaFechaInicio = reserva.getFecha_inicio();
//	                LocalDate reservaFechaFin = reserva.getFecha_fin();
//
//	                // Check if the given dates overlap with any existing reservation
//	                if (!(fechaFin.isBefore(reservaFechaInicio) || fechaIncio.isAfter(reservaFechaFin))) {
//	                    isAvailable = false;
//	                    break; // Stop checking further reservations
//	                }
//	            }
//	        }
//	    }

		
	

}
