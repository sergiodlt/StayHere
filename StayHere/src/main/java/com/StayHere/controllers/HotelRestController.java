package com.StayHere.controllers;


import java.util.ArrayList;
import java.util.List;

import com.StayHere.entities.Comodidad;
import com.StayHere.entities.Foto;
import com.StayHere.entities.Hotel;
import com.StayHere.repositories.HotelRepository;
import com.StayHere.services.ComodidadService;
import com.StayHere.services.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class HotelRestController {
  
  @Autowired
  private HotelRepository HotelRepository;
  
  @Autowired
  private ComodidadService comodidadService;
  
	/*@PostMapping("/filtrar")
	public List<Hotel> filtrar(@RequestBody List<Long> idComodidades) {
		List<Comodidad> comodidadesFilt= new ArrayList<Comodidad>();
		List<Hotel> hotelesFilt= new ArrayList<Hotel>();
		
		for (Long idComodidad : idComodidades) {
		Comodidad comodidad=	comodidadService.getComodidadById(idComodidad);
		comodidadesFilt.add(comodidad);
		}
		
		List <Hotel> hoteles= HotelRepository.findAll();		
		
		
		
		return hotelesFilt;	
		}*/
  }

  
 

  
  
