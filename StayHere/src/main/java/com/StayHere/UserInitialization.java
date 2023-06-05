package com.StayHere;

import java.util.Collection;
import java.util.Collections;

import org.hibernate.cfg.CollectionSecondPass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.StayHere.entities.Role;
import com.StayHere.entities.User;
import com.StayHere.repositories.RoleRepository;
import com.StayHere.repositories.UserRepository;

@Component
public class UserInitialization implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
    	
    	if (roleRepository.findByName("ROLE_ADMIN") == null) {
            // Crear el nuevo usuario
            Role rol = new Role();
            rol.setName("ROLE_ADMIN");
            roleRepository.save(rol);
        }
    	if (roleRepository.findByName("ROLE_USER") == null) {
    		// Crear el nuevo usuario
    		Role rol = new Role();
    		rol.setName("ROLE_USER");
    		roleRepository.save(rol);
    	}
    	
        // Verificar si ya existe un usuario
        if (userRepository.findByUsername("admin") == null) {
            // Crear el nuevo usuario
            User user = new User();
            user.setUsername("admin");
            // Asignar contraseña encriptada
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEmail("admin@example.com");
            Role rol= roleRepository.findByName("ROLE_ADMIN");
            user.setRoles(Collections.singletonList(rol));
            // Guardar el usuario en la base de datos
            userRepository.save(user);
        }
    }
}







