package com.StayHere.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.StayHere.entities.PasswordResetToken;
import com.StayHere.entities.User;
import com.StayHere.repositories.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenService {
    private final PasswordResetTokenRepository tokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void generatePasswordResetToken(User user) {
        // Generar el token
        String token = UUID.randomUUID().toString();

        // Crear el objeto PasswordResetToken
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);

        // Guardar el token en la base de datos
        tokenRepository.save(passwordResetToken);
    }
}

