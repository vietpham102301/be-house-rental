package com.example.houserental.services.email;

import com.example.houserental.controllers.models.EmailRequest;

import java.util.List;

public interface EmailService {
    int sendEmails(List<EmailRequest> emailRequests);
}
