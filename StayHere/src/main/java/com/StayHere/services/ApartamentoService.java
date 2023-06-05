package com.StayHere.services;

import java.util.ArrayList;
import java.util.List;

import com.StayHere.entities.Ciudad;
import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Reserva;
import com.StayHere.repositories.CiudadRepository;
import com.StayHere.repositories.ComodidadRepository;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.ReservaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApartamentoService {

	@Autowired
	private ApartamentoRepository apartamentoRepository;
	
	@Autowired
	private CiudadRepository ciudadRepository;

	@Autowired
	private ComodidadRepository comodidadRepository;


	public List<Apartamento> getApartamentos() {
		return apartamentoRepository.findAll();
	}

	public void saveApartamento(String nombre,String direccion,int telefono,String correo,String descripcion, int capacidad, int precio, Long idCiudad, List<Long> idComodidades) throws Exception {
		Apartamento apartamento = Apartamento.builder().nombre(nombre).direccion(direccion).telefono(telefono).correo(correo).capacidad(capacidad).descripcion(descripcion).precio(precio).build();
		
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
		} catch (Exception e) {
			throw new Exception("El/la apartamento " + nombre + " ya existe");
		}
	}

	public Apartamento getApartamentoById(Long id) {
		return apartamentoRepository.findById(id).get();
	}

	public void updateApartamento(Long id, String nombre,String direccion,int telefono,String correo,String descripcion,int precio, Long idAlojamiento) throws Exception {
		Apartamento apartamento = apartamentoRepository.findById(id).get();
		apartamento.setNombre(nombre);
		apartamento.setDireccion(direccion);
		apartamento.setTelefono(telefono);
		apartamento.setCorreo(correo);
		apartamento.setDescripcion(descripcion);
		apartamento.setPrecio(precio);
		
		if (apartamento.getCiudad() == null || idAlojamiento != apartamento.getCiudad().getId()) {
			Ciudad ciudad = ciudadRepository.getById(idAlojamiento);
			apartamento.setCiudad(ciudad);
			
		}

		try {
			apartamentoRepository.saveAndFlush(apartamento);
		} catch (Exception e) {
			throw new Exception("El/la apartamento " + nombre + " ya existe");
		}
	}

	public void deleteApartamento(Long id) {
		Apartamento apartamento = apartamentoRepository.findById(id).get();
		apartamentoRepository.delete(apartamento);
	}
}
