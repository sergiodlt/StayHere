package com.StayHere.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.StayHere.entities.Role;
import com.StayHere.repositories.RoleRepository;

import jakarta.transaction.Transactional;

	@Service
	@Transactional
	public class RoleService {

	    @Autowired
	    RoleRepository rolRepository;

	    public void save(Role rol){
	        rolRepository.save(rol);
	    }

	    public Role getByName(String nombre){
	        return rolRepository.findByName(nombre);
	    }

	    public boolean existsByName(Role role){
	        return rolRepository.existsByName(role.getName());
	    }
}