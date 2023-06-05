package com.StayHere.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.StayHere.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	    public Role findByName(String Name);
	    
	    boolean existsByName(String Name);
	
}
