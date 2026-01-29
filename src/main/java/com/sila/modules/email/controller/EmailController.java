package com.sila.modules.email.controller;

import com.sila.modules.email.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/sent")
    public String testEmail() {
        emailService.sendEmail(
                "las239879@gmail.com",
                "Hello from Spring Boot",
                "This email is sent via Lacy-Restaurant"
        );
        return "Email sent!";
    }
}