package com.medicconnect.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailUser;

    @Value("${spring.mail.password}")
    private String emailPass;

    // ---------- SMTP Session ----------
    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUser, emailPass);
            }
        });
    }

    // ---------- Send email ----------
    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = new MimeMessage(getSession());
        try {
            message.setFrom(new InternetAddress(emailUser, "Medic-connect"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            message.setFrom(new InternetAddress(emailUser)); // fallback
        }

        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setSentDate(new Date());
        message.setContent(htmlBody, "text/html; charset=utf-8");

        Transport.send(message);
        System.out.println("[EmailService] Email sent to " + to);
    }

    // ---------- Generate OTP email body ----------
    public String generateOtpEmailBody(String name, String otp) {
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.08);">
                <h2 style="color:#0077ff;">Medic-connect OTP Verification</h2>
                <p>Hello <strong>%s</strong>,</p>
                <p>Your verification OTP is:</p>
                <h1 style="text-align:center; color:#0077ff; letter-spacing:2px;">%s</h1>
                <p style="text-align:center;"><strong>Valid for 10 minutes only.</strong></p>
                <div style="margin-top:20px; font-size:12px; color:#666;">
                    &copy; Medic-connect 2025. Do not share this OTP with anyone.
                </div>
            </div>
        </body>
        </html>
        """.formatted(name, otp);
    }

    // ---------- Generate Registration Success email body ----------
    public String generateRegistrationEmailBody(String name, String type, String email) {
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.08);">
                <h2 style="color:#0077ff;">Registration Successful</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your <strong>%s</strong> account has been successfully created on Medic-connect.</p>
                <p><strong>Email:</strong> %s</p>
                <div style="margin-top:20px; padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    You can now log in using your registered email. Keep your credentials secure.
                </div>
                <div style="margin-top:20px; font-size:12px; color:#666;">
                    &copy; Medic-connect 2025
                </div>
            </div>
        </body>
        </html>
        """.formatted(name, type, email);
    }
}
