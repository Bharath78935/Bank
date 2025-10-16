package com.example.bank.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class EmailService {
    private static final Logger logger = LogManager.getLogger(EmailService.class);

    private final String username;
    private final String password;
    private final Session session;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    public void sendEmail(String customerEmail, String toEmail, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username,"Bank"));
            
            message.setReplyTo(new Address[]{new InternetAddress(customerEmail)});

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            logger.info("Email sent TO {} with reply-to {}", toEmail, customerEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to {} from customer {}", toEmail, customerEmail, e);
        }
    }
}
