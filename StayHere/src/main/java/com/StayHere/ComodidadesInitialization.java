package com.StayHere;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.StayHere.entities.Comodidad;
import com.StayHere.repositories.ComodidadRepository;

@Component
public class ComodidadesInitialization implements CommandLineRunner {

	@Autowired
	private ComodidadRepository comodidadRepository;

	@Override
	public void run(String... args) throws Exception {

		// List<Comodidad> comodidades = new ArrayList<Comodidad>();
		List<String> elementos = Arrays.asList("Servicio de Limpieza", "Aire Acondicionado", "Calefaccion", "Telefono",
				"Caja Fuerte", "Wifi", "Televisión", "Minibar", "Piscina", "Baño Privado", "Parking",
				"Ascensor", "Admite Mascotas");
		List<String> iconos = Arrays.asList("fa-solid fa-broom","fa-solid fa-snowflake", "fa-solid fa-temperature-full", "fa-solid fa-phone",
				"fa-solid fa-toolbox", "fa-solid fa-wifi", "fa-solid fa-tv", "fa-solid fa-martini-glass", "fa-solid fa-person-swimming",  "fa-solid fa-toilet", "fa-solid fa-square-parking",
				"fa-solid fa-elevator", "fa-solid fa-paw");
		
		HashMap<String, String> hashMap = new HashMap<>();
		for (int i = 0; i < elementos.size(); i++) {
		    hashMap.put(elementos.get(i), iconos.get(i));
		}

		for (Map.Entry<String, String> entry : hashMap.entrySet()) {
		    String elemento = entry.getKey();
		    if (comodidadRepository.findBynombre(elemento) == null) {
		        Comodidad comodidad = new Comodidad();
		        comodidad.setNombre(elemento);
		        comodidad.setIcono(entry.getValue());
		        comodidadRepository.saveAndFlush(comodidad);
		    }
		}


	}
}
