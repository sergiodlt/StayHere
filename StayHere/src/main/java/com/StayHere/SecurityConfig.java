package com.StayHere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.StayHere.services.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	
	  	@Autowired
	    private UserService userService;
	  
	  	@Bean
	    public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
		@Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
	        auth.setUserDetailsService(userService);
	        auth.setPasswordEncoder(passwordEncoder());
	       
	        return auth;
	    }
		
	  	
	@Bean
	DefaultSecurityFilterChain filterChain (HttpSecurity http, AuthenticationManager authManager) throws Exception {
	     return http
	      .csrf().disable()
          .authorizeHttpRequests()
          .requestMatchers("/reservasAdmin/r").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/hoteles/rAdmin").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/hoteles/uPostAdmin").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/apartamento/rAdmin").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/apartamento/uAdmin").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/habitacion/uAdmin").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/usuarios/r").hasRole("ADMIN") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          
          
          .requestMatchers("/reserva/r").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL para usuarios con rol ADMIN o USER
          .requestMatchers("/usuarios/uPerfil").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL para usuarios con rol ADMIN o USER
          .requestMatchers("/hoteles/r").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/hoteles/c").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/hoteles/u").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/habitacion/r").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/habitacion/c").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/habitacion/u").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/apartamento/r").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/apartamento/c").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .requestMatchers("/apartamento/u").hasAnyRole("ADMIN", "USER") // Restringir acceso a la URL solo para usuarios con rol ADMIN
          // Restringir acceso a la URL solo para usuarios con rol ADMIN
          .anyRequest().permitAll()
          .and() 
          .formLogin()
          .loginPage("/login").permitAll()
          .and()
          .logout()
          .invalidateHttpSession(true)
          .clearAuthentication(true)
          .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
          .logoutSuccessUrl("/")
          .permitAll()
          .and()
          .exceptionHandling()
          .accessDeniedPage("/error") // Página a la que se redirigirá en caso de acceso denegado
          .and()
          .build();
	}
	
	@Bean
	UserDetailsService userDetailsService() {
	     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
	     manager.createUser(User.withUsername("admin")
	    		 .password(passwordEncoder().encode("admin"))
	             .roles()
	             .build());
	     return manager;
	}
	

	  @Bean
	  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
	    return config.getAuthenticationManager();
	  }
	
	


}
