package com.sila.controller;

import com.sila.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/send")
    public String sendTestEmail() {
        emailService.sendEmail("silam8422@gmail.com", "Email for restaurant <Lucy>", "Hello from Spring Boot & Mailtrap!");
        return "Email sent!";
    }
}