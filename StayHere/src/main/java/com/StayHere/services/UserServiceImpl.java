package com.StayHere.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.StayHere.entities.Role;
import com.StayHere.entities.User;
import com.StayHere.repositories.RoleRepository;
import com.StayHere.repositories.UserRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Override
    public void save(User user) {
        userRepository.save(user);
        emailService.sendRegistrationEmail(user.getEmail());
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
 /* public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
 
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
 
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));		
}*/
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  User user = userRepository.findByUsername(username);
	  if (user == null) {
		  throw new UsernameNotFoundException("User not found");
	  }
	  if (user.getRoles().isEmpty()) {
	        Role userRole = roleRepository.findByName("ROLE_USER");
	        user.getRoles().add(userRole);
	    }
	  
	  return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), mapRolesToAuthorities(user.getRoles()));	

  }
  
  public void updateUser(User user, String Username, /*String Password,*/ String direccion, String email, String FirtsName, String LastName, String Telefono, String genero) throws Exception {
	  
	  	user.setUsername(Username);
	  /*user.setPassword(Password);*/
	    user.setDireccion(direccion);
	    user.setEmail(email);
	    user.setFirstName(FirtsName);
	    user.setLastName(LastName);
	    user.setTelefono(Telefono);
	    user.setGenero(genero);
	    
	    
	    try {
	    	userRepository.saveAndFlush(user);
	    	} catch (Exception e) {
			throw new Exception("No se ha podido modificar el usuario");
		}
	}

private List<SimpleGrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
	return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
}

	
}
