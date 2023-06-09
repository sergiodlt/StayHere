package com.StayHere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.StayHere.entities.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{
	
	
}