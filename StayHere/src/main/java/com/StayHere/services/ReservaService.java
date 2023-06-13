package com.StayHere.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
import org.springframework.scheduling.annotation.Async;
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
	
	@Autowired
	private EmailService emailService;
	
	public List<Reserva> getReservas() {
		return reservaRepository.findAll();
	}

	public void saveReservaHotel(LocalDate fecha_inicio,LocalDate fecha_fin, User user, Long idHabitacion, int huespedes, int precio) throws Exception {
		Reserva reserva = Reserva.builder().fecha_inicio(fecha_inicio).fecha_fin(fecha_fin).huespedes(huespedes).user(user).build();
		 int precioTotal = precio * (int) ChronoUnit.DAYS.between(fecha_inicio, fecha_fin);
		    reserva.setPrecio(precioTotal);
	
		/*
		Apartamento apartamento = apartamentoRepository.getById(idApartamento);
		reserva.setApartamento(apartamento);
		*/
		Habitacion habitacion = habitacionService.getHabitacionById(idHabitacion);
		reserva.setHabitacion(habitacion);
		
		
		try {
			reservaRepository.saveAndFlush(reserva);
			enviarEmailReservaHotel(user, fecha_inicio, fecha_fin, huespedes, habitacion, precioTotal);
		} catch (Exception e) {
			throw new Exception("El/la reserva de esta habitacion ya existe");
		}
	}
	
	public void saveReservaApartamento(LocalDate fecha_inicio,LocalDate fecha_fin, User user, Long idApartamento, int huespedes, int precio) throws Exception {
		Reserva reserva = Reserva.builder().fecha_inicio(fecha_inicio).fecha_fin(fecha_fin).huespedes(huespedes).user(user).build();
		 int precioTotal = precio * (int) ChronoUnit.DAYS.between(fecha_inicio, fecha_fin);
		    reserva.setPrecio(precioTotal);
	
	
		/*
		Apartamento apartamento = apartamentoRepository.getById(idApartamento);
		reserva.setApartamento(apartamento);
		*/
		Apartamento apartamento = apartamentoService.getApartamentoById(idApartamento);
		reserva.setApartamento(apartamento);
		
		
		try {
			reservaRepository.saveAndFlush(reserva);
			enviarEmailReservaApartamento(user, fecha_inicio, fecha_fin, huespedes, apartamento, precioTotal);
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
	
	@Async
	public void enviarEmailReservaHotel(User user, LocalDate fecha_inicio, LocalDate fecha_fin, int huespedes, Habitacion habitacion, int precio) {

	 String asunto = "Reserva realiza con exito";
     String destinatario = user.getEmail(); // Tu dirección de correo electrónico
     LocalDate fechaInicio = fecha_inicio;
     LocalDate fechaFin = fecha_fin;
     int numeroHuespedes = huespedes;
     double precioTotal = precio;
     String nombreHotel = habitacion.getHotel().getNombre();
     Long Nhabitacion= habitacion.getId();
     String emailContacto = "stayhere.tfg@gmail.com";

     String body = "<html>\n" +
             "<head>\n" +
             "<style>\n" +
             "body {\n" +
             "    font-family: Arial, sans-serif;\n" +
             "    background-color: #6DDAF8;\n" +
             "}\n" +
             ".container {\n" +
             "    max-width: 600px;\n" +
             "    margin: 0 auto;\n" +
             "    padding: 20px;\n" +
             "    background-color: #ffffff;\n" +
             "    border-radius: 5px;\n" +
             "    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
             "}\n" +
             ".header {\n" +
             "    text-align: center;\n" +
             "    margin-bottom: 30px;\n" +
             "}\n" +
             ".header img {\n" +
             "    max-width: 200px;\n" +
             "}\n" +
             ".header h2 {\n" +
             "    color: #1e90ff;\n" +
             "}\n" +
             ".content {\n" +
             "    margin-top: 30px;\n" +
             "    background-color: #f2f2f2;\n" +
             "    padding: 20px;\n" +
             "    border-radius: 5px;\n" +
             "}\n" +
             ".reservation-details {\n" +
             "    margin-bottom: 20px;\n" +
             "}\n" +
             ".reservation-details h3 {\n" +
             "    color: #1e90ff;\n" +
             "    margin-bottom: 10px;\n" +
             "}\n" +
             ".reservation-details p {\n" +
             "    margin-bottom: 5px;\n" +
             "}\n" +
             ".button {\n" +
             "    display: inline-block;\n" +
             "    padding: 10px 20px;\n" +
             "    background-color: #1e90ff;\n" +
             "    color: #ffffff;\n" +
             "    text-decoration: none;\n" +
             "    border-radius: 5px;\n" +
             "}\n" +
             "</style>\n" +
             "</head>\n" +
             "<body>\n" +
             "<div class=\"container\">\n" +
             "    <div class=\"header\">\n" +
             "        <h2>Detalles de la Reserva</h2>\n" +
             "    </div>\n" +
             "    <div class=\"content\">\n" +
             "        <div class=\"reservation-details\">\n" +
             "            <h3>Información de la Reserva</h3>\n" +
             "            <h4>Hotel: "+nombreHotel +"</h4>\n" +
             "            <p><strong>Fecha de inicio:</strong> " + fechaInicio + "</p>\n" +
             "            <p><strong>Fecha de fin:</strong> " + fechaFin + "</p>\n" +
             "            <p><strong>Huéspedes:</strong> " + numeroHuespedes + "</p>\n" +
             "        </div>\n" +
             "        <div class=\"reservation-details\">\n" +
             "            <h3>Datos del Alojamiento</h3>\n" +
             "            <p><strong>Nº Habitacón:</strong> " + Nhabitacion + "</p>\n" +
             "            <p><strong>Precio:</strong> " + precioTotal + "€</p>\n" +
             "        </div>\n" +
             "        <div class=\"reservation-details\">\n" +
             "            <h3>Información de Contacto</h3>\n" +
             "            <p><strong>Email:</strong> " + emailContacto + "</p>\n" +
             "        </div>\n" +
             "    </div>\n" +
             "</div>\n" +
             "</body>\n" +
             "</html>";

     emailService.enviarCorreo(asunto, destinatario, body);
	}
	@Async
	public void enviarEmailReservaApartamento(User user, LocalDate fecha_inicio, LocalDate fecha_fin, int huespedes, Apartamento apartamento, int precio) {
		
		String asunto = "Reserva realiza con exito";
		String destinatario = user.getEmail(); // Tu dirección de correo electrónico
		LocalDate fechaInicio = fecha_inicio;
		LocalDate fechaFin = fecha_fin;
		int numeroHuespedes = huespedes;
		double precioTotal = precio;
		String nombreApartamento = apartamento.getNombre();
		String emailContacto = "stayhere.tfg@gmail.com";
		
		
		
		String body = "<html>\n" +
				"<head>\n" +
				"<style>\n" +
				"body {\n" +
				"    font-family: Arial, sans-serif;\n" +
				"    background-color: #6DDAF8;\n" +
				"}\n" +
				".container {\n" +
				"    max-width: 600px;\n" +
				"    margin: 0 auto;\n" +
				"    padding: 20px;\n" +
				"    background-color: #ffffff;\n" +
				"    border-radius: 5px;\n" +
				"    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
				"}\n" +
				".header {\n" +
				"    text-align: center;\n" +
				"    margin-bottom: 30px;\n" +
				"}\n" +
				".header img {\n" +
				"    max-width: 200px;\n" +
				"}\n" +
				".header h2 {\n" +
				"    color: #1e90ff;\n" +
				"}\n" +
				".content {\n" +
				"    margin-top: 30px;\n" +
				"    background-color: #f2f2f2;\n" +
				"    padding: 20px;\n" +
				"    border-radius: 5px;\n" +
				"}\n" +
				".reservation-details {\n" +
				"    margin-bottom: 20px;\n" +
				"}\n" +
				".reservation-details h3 {\n" +
				"    color: #1e90ff;\n" +
				"    margin-bottom: 10px;\n" +
				"}\n" +
				".reservation-details p {\n" +
				"    margin-bottom: 5px;\n" +
				"}\n" +
				".button {\n" +
				"    display: inline-block;\n" +
				"    padding: 10px 20px;\n" +
				"    background-color: #1e90ff;\n" +
				"    color: #ffffff;\n" +
				"    text-decoration: none;\n" +
				"    border-radius: 5px;\n" +
				"}\n" +
				"</style>\n" +
				"</head>\n" +
				"<body>\n" +
				"<div class=\"container\">\n" +
				"    <div class=\"header\">\n" +
				"        <h2>Detalles de la Reserva</h2>\n" +
				"    </div>\n" +
				"    <div class=\"content\">\n" +
				"        <div class=\"reservation-details\">\n" +
				"            <h3>Información de la Reserva</h3>\n" +
				"            <h4>Apartamento: "+nombreApartamento +"</h4>\n" +
				"            <p><strong>Fecha de inicio:</strong> " + fechaInicio + "</p>\n" +
				"            <p><strong>Fecha de fin:</strong> " + fechaFin + "</p>\n" +
				"            <p><strong>Huéspedes:</strong> " + numeroHuespedes + "</p>\n" +
				"        </div>\n" +
				"        <div class=\"reservation-details\">\n" +
				"            <h3>Datos del Alojamiento</h3>\n" +
				"            <p><strong>Precio:</strong> " + precioTotal + "€</p>\n" +
				"        </div>\n" +
				"        <div class=\"reservation-details\">\n" +
				"            <h3>Información de Contacto</h3>\n" +
				"            <p><strong>Email:</strong> " + emailContacto + "</p>\n" +
				"        </div>\n" +
				"    </div>\n" +
				"</div>\n" +
				"</body>\n" +
				"</html>";
		
		emailService.enviarCorreo(asunto, destinatario, body);
	}
}
