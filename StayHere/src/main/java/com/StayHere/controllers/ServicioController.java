package com.StayHere.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ServicioController {

	 @GetMapping("/services")
	    public String servicios(
	    		ModelMap m) {
			 m.put("view", "services");
		        return "_t/frame";
		    }
	
}
