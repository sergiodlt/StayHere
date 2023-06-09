package com.StayHere.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@Builder


@Entity
public class PasswordResetToken {
    private static final int EXPIRATION_TIME = 60; // Tiempo de expiración en minutos

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expirationDateTime;

    public PasswordResetToken() {
        this.expirationDateTime = calculateExpirationDateTime();
    }

    // Constructor, getters y setters
    // ...
    
    private LocalDateTime calculateExpirationDateTime() {
        return LocalDateTime.now().plusMinutes(EXPIRATION_TIME);
    }
}
