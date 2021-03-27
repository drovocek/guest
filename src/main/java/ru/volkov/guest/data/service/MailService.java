package ru.volkov.guest.data.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSender mailSender;

    public void sendMessage(String targetMail, String body, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("guest@app.com");
        message.setTo(targetMail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
