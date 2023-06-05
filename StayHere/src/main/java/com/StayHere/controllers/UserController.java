

package com.StayHere.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.StayHere.entities.Role;
import com.StayHere.entities.User;
import com.StayHere.repositories.UserRepository;
import com.StayHere.services.RoleService;
import com.StayHere.services.UserService;
import com.StayHere.services.UserServiceImpl;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleService roleService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserServiceImpl userImpl;
    
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") @Validated User user, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role rolUser = roleService.getByName("ROLE_USER");
        Set<Role> roles = new HashSet();
        roles.add(rolUser);
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/login?success";
    }
    
    @GetMapping("/usuarios/r")
    public String r (ModelMap m) {
    	List<User> users= userRepository.findAll();
        m.put("users", users);
		m.put("view", "usuario/r");

		return "_t/frame"; 
		 }
    @PostMapping("/usuarios/u")
    public String u (@RequestParam ("idUser") Long idUser,
    		ModelMap m) {
    	User user= userRepository.getById(idUser);
        m.put("usuario", user);
		m.put("view", "usuario/u");

		return "_t/frame"; 
		 }
    
    @PostMapping("/usuarios/uPost")
    public String u (@RequestParam ("idUser") Long idUser,
    		@RequestParam("username") String username,
    		@RequestParam(required=false,name="password") String password,
    		@RequestParam("nombre") String nombre,
    		@RequestParam("apellido") String apellido,
    		@RequestParam("genero") String genero,
    		@RequestParam("email") String email,
    		@RequestParam("direccion") String direccion,
    		@RequestParam("telefono") String telefono,
    		ModelMap m) throws Exception {
    	
    	User user= userRepository.getById(idUser);
        
    	userImpl.updateUser(user, username,/* passwordEncoder.encode(password),*/ direccion, email, nombre, apellido, telefono, genero);

		m.put("view", "/home");
		return "_t/frame"; 
		 }
    
    
   
   

}
