package com.StayHere.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsController {

	 @GetMapping("/aboutUs")
	    public String aboutUs(
	    		ModelMap m) {
			 m.put("view", "aboutUs");
		        return "_t/frame";
		    }
}
