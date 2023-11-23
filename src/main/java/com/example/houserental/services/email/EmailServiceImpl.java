package com.example.houserental.services.email;
import com.example.houserental.controllers.models.EmailRequest;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;

    public int sendEmails(List<EmailRequest> emailRequests) {
        int successfullySent = 0;

        for (EmailRequest emailRequest : emailRequests) {
            if (sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getContent())) {
                successfullySent++;
            }
        }

        return successfullySent;
    }

    private boolean sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set to true for HTML content
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}