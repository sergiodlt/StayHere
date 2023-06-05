package com.StayHere.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.StayHere.entities.User;


public interface UserService extends UserDetailsService{

    Optional<User> findByEmail(String email);

    void save(User user);

    
}
