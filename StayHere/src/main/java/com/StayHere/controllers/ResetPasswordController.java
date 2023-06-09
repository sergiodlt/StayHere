package com.StayHere.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.StayHere.entities.User;
import com.StayHere.repositories.UserRepository;

import ch.qos.logback.core.model.Model;

@Controller
public class ResetPasswordController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private MailSender mailSender;

    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        // Mostrar formulario de solicitud de restablecimiento de contraseña
        return "usuario/forgot";
    }

    @PostMapping("/reset-password")
    public String processResetPasswordRequest(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            // Generar un token de restablecimiento de contraseña y enviar un correo electrónico con un enlace
            String resetToken = generateResetToken();
            user.setResetToken(resetToken);
            userRepository.save(user);
            sendResetPasswordEmail(user, resetToken);
        }
        return "redirect:/login?resetSuccess";
    }

    @GetMapping("/reset-password/confirm")
    public String showResetPasswordConfirmForm(@RequestParam("token") String token, ModelMap model) {
        User user = userRepository.findByResetToken(token).orElse(null);
        if (user == null) {
            // Token inválido, redireccionar a una página de error o mostrar un mensaje de error
            return "redirect:/error";
        }
        model.put("token", token); // Pasar el token al modelo para que esté disponible en la vista
        return "usuario/reset";
    }


    @PostMapping("/reset-password/confirm")
    public String processResetPasswordConfirmRequest(@RequestParam("token") String token, @RequestParam("password") String password) {
        User user = userRepository.findByResetToken(token).orElse(null);
        if (user == null) {
            // Token inválido, redireccionar a una página de error o mostrar un mensaje de error
            return "redirect:/error";
        }

        

        // Restablecer la contraseña encriptada y eliminar el token de restablecimiento
        user.setPassword(passwordEncoder.encode(password));
        user.setResetToken(null);
        userRepository.save(user);

        return "redirect:/login?passwordResetSuccess";
    }

    private String generateResetToken() {
        // Generar un token único para restablecimiento de contraseña
        return UUID.randomUUID().toString();
    }

    @Async
    private void sendResetPasswordEmail(User user, String resetToken) {
        // Envía un correo electrónico al usuario con un enlace para restablecer la contraseña
        String resetLink = "http://localhost:8080/reset-password/confirm?token=" + resetToken;
        String emailBody = "Hola " + user.getUsername() + ",\n\n"
                + "Recibimos una solicitud para restablecer tu contraseña. Si no realizaste esta solicitud, puedes ignorar este correo electrónico.\n\n"
                + "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n\n"
                + resetLink + "\n\n"
                + "Gracias,\n"
                + "Tu aplicación";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Restablecimiento de contraseña");
        mailMessage.setText(emailBody);
        mailSender.send(mailMessage);
    }

}
