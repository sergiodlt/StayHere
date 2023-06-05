package com.StayHere;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
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
	             .build();
	     
	    /* return http
	    		 .authorizeRequests()
	                .requestMatchers("/", "/home").permitAll()
	                .anyRequest().authenticated()
	                .and()
	            .formLogin()
	                .loginPage("/login")
	                .permitAll()
	                .and()
	            .logout()
	                .permitAll();
	    		 
	             */
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
