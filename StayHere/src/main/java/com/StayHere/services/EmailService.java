package com.StayHere.services;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    	 public void sendRegistrationEmail(String recipientEmail) {
    	        String subject = "¡Registro exitoso!";
    	        String body = "<html>\n" +
    	                "<head>\n" +
    	                "<style>\n" +
    	                "body {\n" +
    	                "    font-family: Arial, sans-serif;\n" +
    	                "    background-color: #6DDAF8;\n" +
    	                "}\n" +
    	                ".container {\n" +
    	                "    max-width: 600px;\n" +
    	                "    margin: 0 auto;\n" +
    	                "    padding: 20px;\n" +
    	                "    background-color: #ffffff;\n" +
    	                "    border-radius: 5px;\n" +
    	                "    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);\n" +
    	                "}\n" +
    	                ".header {\n" +
    	                "    text-align: center;\n" +
    	                "    margin-bottom: 30px;\n" +
    	                "}\n" +
    	                ".header img {\n" +
    	                "    max-width: 200px;\n" +
    	                "}\n" +
    	                ".header h2 {\n" +
    	                "    color: #1e90ff;\n" +
    	                "}\n" +
    	                ".content {\n" +
    	                "    margin-top: 30px;\n" +
    	                "    background-color: #f2f2f2;\n" +
    	                "    padding: 20px;\n" +
    	                "    border-radius: 5px;\n" +
    	                "}\n" +
    	                ".button {\n" +
    	                "    display: inline-block;\n" +
    	                "    padding: 10px 20px;\n" +
    	                "    background-color: #1e90ff;\n" +
    	                "    color: #ffffff;\n" +
    	                "    text-decoration: none;\n" +
    	                "    border-radius: 5px;\n" +
    	                "}\n" +
    	                "</style>\n" +
    	                "</head>\n" +
    	                "<body>\n" +
    	                "<div class=\"container\">\n" +
    	                "    <div class=\"header\">\n" +
    	                "        <h2>Bienvenido a StayHere</h2>\n" +
    	                "    </div>\n" +
    	                "    <div class=\"content\">\n" +
    	                "        <p>Gracias por registrarte en nuestra aplicación StayHere. Estamos encantados de tenerte como parte de nuestra comunidad.</p>\n" +
    	                "        <p>Te invitamos a comenzar a explorar todas las increíbles características que ofrecemos. ¡No te lo pierdas!</p>\n" +
    	                "        <a href=\"https://stayhere-tfg-production.up.railway.app/login\" class=\"button\">Iniciar sesión</a>\n" +
    	                "    </div>\n" +
    	                "</div>\n" +
    	                "</body>\n" +
    	                "</html>";

    	        MimeMessage message = mailSender.createMimeMessage();
    	        MimeMessageHelper helper;
    	        try {
    	            helper = new MimeMessageHelper(message, true);
    	            helper.setTo(recipientEmail);
    	            helper.setSubject(subject);
    	            helper.setText(body, true);
    	            mailSender.send(message);
    	        } catch (MessagingException e) {
    	            e.printStackTrace();
    	        }
    	    }
    
    
    @Async
    public void enviarCorreo(String asunto, String destinatario, String mensaje) {
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setSubject(asunto);
        correo.setTo(destinatario);
        correo.setText(mensaje);
        
        mailSender.send(correo);
    }
}

