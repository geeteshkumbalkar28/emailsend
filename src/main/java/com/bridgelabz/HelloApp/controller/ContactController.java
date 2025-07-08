package com.bridgelabz.HelloApp.controller;

import com.bridgelabz.HelloApp.model.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "*") // Allow CORS for all origins
public class ContactController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping
    public ResponseEntity<String> handleContact(@RequestBody ContactRequest request) {
        if (request.getName() == null || request.getEmail() == null ||
                request.getPhone() == null || request.getMessage() == null) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"All fields are required.\"}");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("info.sairuraldevelopmenttrust@alphaseam.com");
            helper.setTo("info.sairuraldevelopmenttrust@alphaseam.com");
            helper.setSubject("New Contact Form Submission");
            helper.setText(
                    "<h2>New Contact Message from sairuraldevelopmenttrust</h2>" +
                            "<p><strong>Name:</strong> " + request.getName() + "</p>" +
                            "<p><strong>Email:</strong> " + request.getEmail() + "</p>" +
                            "<p><strong>Phone:</strong> " + request.getPhone() + "</p>" +
                            "<p><strong>Message:</strong><br/>" + request.getMessage() + "</p>",
                    true
            );

            mailSender.send(message);
            return ResponseEntity.ok("{\"success\": true, \"message\": \"Email sent successfully.\"}");

        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("{\"success\": false, \"message\": \"Failed to send email.\"}");
        }
    }
}