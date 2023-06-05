package com.StayHere;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.StayHere.entities.Ciudad;
import com.StayHere.repositories.CiudadRepository;

@Component
public class CiudadesInitialization implements CommandLineRunner {

	@Autowired
	private CiudadRepository ciudadRepository;

	@Override
	public void run(String... args) throws Exception {

		List<String> elementos = Arrays.asList("Álava", "Albacete", "Alicante", "Almería", "Asturias", "Ávila", "Badajoz", "Barcelona", "Burgos", "Cáceres", "Cádiz",
				"Cantabria", "Castellón", "Ciudad Real", "Córdoba", "Cuenca", "Gerona (Girona)", "Granada", "Guadalajara", "Guipúzcoa (Gipuzkoa)", "Huelva", "Huesca",
				"Islas Baleares (Balearic Islands)", "Jaén", "La Coruña (A Coruña)", "La Rioja", "Las Palmas", "León", "Lérida (Lleida)", "Lugo", "Madrid", "Málaga", 
				"Murcia", "Navarra", "Orense (Ourense)", "Palencia", "Pontevedra", "Salamanca", "Santa Cruz de Tenerife", "Segovia", "Sevilla", "Soria", "Tarragona", 
				"Teruel", "Toledo", "Valencia", "Valladolid", "Vizcaya (Bizkaia)", "Zamora", "Zaragoza", "Ceuta (Ciudad Autónoma de Ceuta)", "Melilla (Ciudad Autónoma de Melilla)");

		// Crear un ArrayList a partir de la lista existente
		ArrayList<String> miArrayList = new ArrayList<>(elementos);
		for (String elemento : miArrayList) {
			if (ciudadRepository.findBynombre(elemento) == null) {
				Ciudad ciudad = new Ciudad();
				ciudad.setNombre(elemento);
				ciudadRepository.saveAndFlush(ciudad);
			}
		}

	}
}
