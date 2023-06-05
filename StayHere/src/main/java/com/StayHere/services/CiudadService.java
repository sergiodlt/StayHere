package com.StayHere.services;

import java.util.List;
import com.StayHere.entities.Ciudad;
import com.StayHere.repositories.CiudadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CiudadService {

	@Autowired
	private CiudadRepository ciudadRepository;

	public List<Ciudad> getCiudades() {
		return ciudadRepository.findAll();
	}

	public void saveCiudad(String nombre) throws Exception {
		Ciudad ciudad = Ciudad.builder().nombre(nombre).build();
		try {
			ciudadRepository.saveAndFlush(ciudad);
		} catch (Exception e) {
			throw new Exception("La ciudad " + nombre + " ya existe");
		}
	}

	public Ciudad getCiudadById(Long id) {
		return ciudadRepository.findById(id).get();
	}

	public void updateCiudad(Long id, String nombre) throws Exception {
		Ciudad ciudad = ciudadRepository.findById(id).get();
		ciudad.setNombre(nombre);
		try {
			ciudadRepository.saveAndFlush(ciudad);
		} catch (Exception e) {
			throw new Exception("El/la alojamiento " + ciudad + " ya existe");
		}
	}

	public void deleteCiudad(Long id) {
		Ciudad ciudad = ciudadRepository.findById(id).get();
		ciudadRepository.delete(ciudad);
	}
}
