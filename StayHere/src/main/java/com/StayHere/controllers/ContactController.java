package com.StayHere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.StayHere.entities.CorreoForm;
import com.StayHere.services.EmailService;

@Controller
public class ContactController {

	
	 @Autowired
	    private EmailService emailService;

	 @GetMapping("/contacto")
	    public String contacto(
	    		ModelMap m) {
		 m.put("view", "contacto");
	        return "_t/frame";
	    }
	    
	    @PostMapping("/enviarCorreo")
	    public String enviarCorreo(
	    		@RequestParam("nombre") String nombre,
	    		@RequestParam("email") String email,
	    		@RequestParam("mensaje") String msj,
	    		ModelMap m
	    		) {
	        String asunto = "Nuevo mensaje de contacto";
	        String destinatario = "stayhere.tfg@gmail.com"; // Tu dirección de correo electrónico
	        String mensaje = "Nombre: " +   nombre + "\n"
	                + "Correo electrónico: " + email + "\n"
	                + "Mensaje: " + msj;

	        emailService.enviarCorreo(asunto, destinatario, mensaje);
	        m.put("view", "home");
	        return "_t/frame";
	    }
	    
	}