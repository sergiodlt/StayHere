

package com.StayHere.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.StayHere.entities.Apartamento;
import com.StayHere.entities.Hotel;
import com.StayHere.entities.Reserva;
import com.StayHere.entities.Role;
import com.StayHere.entities.User;
import com.StayHere.repositories.ApartamentoRepository;
import com.StayHere.repositories.HotelRepository;
import com.StayHere.repositories.ReservaRepository;
import com.StayHere.repositories.UserRepository;
import com.StayHere.services.ApartamentoService;
import com.StayHere.services.HotelService;
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
    private HotelService hotelService;

    @Autowired
    private ApartamentoService apartamentoService;

    @Autowired
    private ApartamentoRepository apartamentoRepository;

    @Autowired
    private ReservaRepository reservaRepository;
    
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
  
    
    @PostMapping("/usuario/uPost")
    public String uPost (@RequestParam ("idUser") Long idUser,
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

        userImpl.updateUser(user, username, direccion, email, nombre, apellido, telefono, genero);

        return "redirect:/"; 
         }

    @PostMapping("/usuario/d")
    public String uPost (@RequestParam ("id") Long idUser) throws Exception {
    	User user=userRepository.getById(idUser);
    	
    	for(Hotel hotel : user.getHoteles()) {
    		hotelService.deleteHotel(hotel.getId());
    	}
    	
    	for(Apartamento apartamento : user.getApartamentos()) {
    		apartamentoService.deleteApartamento(apartamento.getId());
    	}
    	for(Reserva reserva : user.getReservas()) {
    		reservaRepository.delete(reserva);
    	}
    	userRepository.deleteById(idUser);
    	return "redirect:/usuarios/r"; 
    }
    
    

    @PostMapping("/verificar-email")
    public @ResponseBody boolean verificarEmailExistente(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        return user != null;
    }

    @GetMapping("/usuarios/uPerfil")
    public String uPerfil (
            ModelMap m) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        m.put("usuario", user);
        m.put("view", "usuario/u");

        return "_t/frame"; 
         }
    
    @PostMapping("/verificar-user")
    public @ResponseBody Map<String, Boolean> verificarEmailExistente(@RequestParam("email") String email,@RequestParam("username") String username,@RequestParam("tlf") String tlf) {
        Boolean existEmail = userRepository.findByEmail(email).orElse(null) != null;
        Boolean existUsername = userRepository.findByUsername(username) != null;
        Boolean existTlf = userRepository.findByTelefono(tlf) != null;
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("existTlf", existTlf);
        response.put("existEmail", existEmail);
        response.put("existUsername", existUsername);
        
        return response;
    }
    
    
    


    
    
   
   

}
