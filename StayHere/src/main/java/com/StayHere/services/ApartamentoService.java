package com.StayHere.services;

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
import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.User;
import com.StayHere.repositories.CiudadRepository;
import com.StayHere.repositories.ComodidadRepository;
import com.StayHere.repositories.FotoRepository;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApartamentoService {

	@Autowired
	private ApartamentoRepository apartamentoRepository;
	
	@Autowired
	private CiudadRepository ciudadRepository;

	@Autowired
	private ComodidadRepository comodidadRepository;

	@Autowired
	private FotoRepository fotoRepository;
	
	@Autowired
	private ReservaRepository reservaRepository;
	
	public List<Apartamento> getApartamentos() {
		return apartamentoRepository.findAll();
	}

	public void saveApartamento(String nombre,String direccion,int telefono,String correo,String descripcion, int capacidad, int precio, Long idCiudad, List<Long> idComodidades, User user, List<MultipartFile> fotos) throws Exception {
		Apartamento apartamento = Apartamento.builder().nombre(nombre).direccion(direccion).telefono(telefono).correo(correo).capacidad(capacidad).descripcion(descripcion).precio(precio).user(user).build();
		
		Ciudad ciudad = ciudadRepository.getById(idCiudad);
		apartamento.setCiudad(ciudad);
		
		List<Comodidad> comodidades = new ArrayList<Comodidad>();
		for(Long idComodidad : idComodidades) {
			Comodidad comodidad = comodidadRepository.getById(idComodidad);
			comodidades.add(comodidad);
		}
		apartamento.setComodidades(comodidades);
		
		
		try {
			apartamentoRepository.saveAndFlush(apartamento);
			for (MultipartFile foto : fotos) {
			    if (!foto.isEmpty()) {
			        try {
			            String nombreArchivo = foto.getOriginalFilename();

			            // Guardar la imagen en la carpeta resources/static/img/
			            Path rutaArchivo = Paths.get("images/" + nombreArchivo);
			            Files.write(rutaArchivo, foto.getBytes());
			            Foto fotoBDD = new Foto();
			            fotoBDD.setRuta("/images/" + nombreArchivo);
			            fotoBDD.setApartamento(obtenerUltimoApartamentoAgregado());
			            fotoRepository.save(fotoBDD);

			        } catch (IOException e) {
			            e.printStackTrace();
			        }
			    }
			}
		} catch (Exception e) {
			throw new Exception("El/la apartamento " + nombre + " ya existe");
		}
	}

	public Apartamento getApartamentoById(Long id) {
		return apartamentoRepository.findById(id).get();
	}

	public void updateApartamento(Long id, String nombre,String direccion,int telefono,String correo,String descripcion,int precio, Long idAlojamiento, int capacidad, List<MultipartFile> fotos) throws Exception {
		Apartamento apartamento = apartamentoRepository.findById(id).get();
		apartamento.setNombre(nombre);
		apartamento.setDireccion(direccion);
		apartamento.setTelefono(telefono);
		apartamento.setCorreo(correo);
		apartamento.setDescripcion(descripcion);
		apartamento.setPrecio(precio);
		apartamento.setCapacidad(capacidad);
		
		
		if (apartamento.getCiudad() == null || idAlojamiento != apartamento.getCiudad().getId()) {
			Ciudad ciudad = ciudadRepository.getById(idAlojamiento);
			apartamento.setCiudad(ciudad);
			
		}
	    if (fotos != null && !fotos.isEmpty()) {

		List<Foto> fotosAntiguas= fotoRepository.findByApartamentoId(id);
		for (Foto foto : fotosAntiguas) {
			fotoRepository.delete(foto);
		}

		
		for (MultipartFile foto : fotos) {
		    if (!foto.isEmpty()) {
		        try {
		            String nombreArchivo = foto.getOriginalFilename();

		            // Guardar la imagen en la carpeta resources/static/img/
		            Path rutaArchivo = Paths.get("images/" + nombreArchivo);
		            Files.write(rutaArchivo, foto.getBytes());
		            Foto fotoBDD = new Foto();
		            fotoBDD.setRuta("/images/" + nombreArchivo);
		            fotoBDD.setApartamento(apartamento);
		            fotoRepository.save(fotoBDD);

		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		}
	    }

		try {
			apartamentoRepository.saveAndFlush(apartamento);
		} catch (Exception e) {
			throw new Exception("El/la apartamento " + nombre + " ya existe");
		}
	}
	
	

	public void deleteApartamento(Long id) {
		Apartamento apartamento = apartamentoRepository.findById(id).get();
		for (Foto foto : apartamento.getFotos()) {
			fotoRepository.delete(foto);
		}

		for (Reserva reserva : apartamento.getReservas()) {
			reservaRepository.delete(reserva);
		}
		apartamentoRepository.delete(apartamento);
	}

	public Apartamento obtenerUltimoApartamentoAgregado() {
		// TODO Auto-generated method stub
		return apartamentoRepository.findFirstByOrderByIdDesc().get();
	}

	

	
}
