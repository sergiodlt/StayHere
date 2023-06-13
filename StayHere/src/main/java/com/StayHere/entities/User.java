package com.StayHere.entities;


import java.io.Serializable;
import java.util.Collection;



import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique=true)
    private String email;

    private String password;

    @Column(unique=true)
    private String username;
    
    private String genero;
    
    private String direccion;
    
    @Column(unique=true)
    private String telefono;
    
    @Column(name = "reset_token")
    private String resetToken;

    @OneToMany(mappedBy = "user")
	private Collection<Hotel> hoteles;
    
    @OneToMany(mappedBy = "user")
   	private Collection<Apartamento> apartamentos;
    
    @ManyToMany
	private Collection<Role> roles;

    // getters and setters
}
