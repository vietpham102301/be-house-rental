package com.example.houserental.controllers.email;

import com.example.houserental.controllers.models.EmailRequest;
import com.example.houserental.services.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email")
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-emails")
    public ResponseEntity<Object> sendEmails(@RequestBody List<EmailRequest> emailRequests) {
        int successfullySent = emailService.sendEmails(emailRequests);

        if (successfullySent > 0) {
            Map<String, String> response = new HashMap<>();
            response.put("message", successfullySent + " emails sent successfully");
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send emails", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
