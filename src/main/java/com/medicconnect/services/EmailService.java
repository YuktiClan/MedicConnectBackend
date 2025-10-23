package com.medicconnect.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailUser;

    @Value("${spring.mail.password}")
    private String emailPass;

    // -----------------------------
    // Get SMTP Session
    // -----------------------------
    private Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailUser, emailPass);
            }
        });
    }

    // -----------------------------
    // Send Email
    // -----------------------------
    public void sendEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = new MimeMessage(getSession());
        try {
            message.setFrom(new InternetAddress(emailUser, "Medic-connect"));
        } catch (Exception e) {
            message.setFrom(new InternetAddress(emailUser));
        }
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setSentDate(java.util.Date.from(LocalDateTime.now().atZone(java.time.ZoneId.systemDefault()).toInstant()));
        message.setContent(htmlBody, "text/html; charset=utf-8");

        Transport.send(message);
        System.out.println("[EmailService] Email sent to " + to);
    }

    // -----------------------------
    // Organization Registration Email
    // -----------------------------
    public String generateOrgRegistrationSuccessEmail(String orgName, String category, String registrationNumber, String orgId) {
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">Organization Successfully Registered</h2>
                <p>Dear Team,</p>
                <p>Your organization has been successfully registered on Medic-connect.</p>
                <div style="padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    <p><strong>Organization Name:</strong> %s</p>
                    <p><strong>Category:</strong> %s</p>
                    <p><strong>Registration Number:</strong> %s</p>
                    <p><strong>Organization ID:</strong> %s</p>
                    <p><strong>Registered On:</strong> %s</p>
                </div>
                <p>Please register yourself as the <strong>Admin</strong> to manage your organization on the Medic-connect Dashboard.</p>
                <p>Welcome to Medic-connect!</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890<br>
                    &copy; 2025 Medic-connect. All rights reserved.
                </p>
            </div>
        </body>
        </html>
        """.formatted(orgName, category, registrationNumber, orgId, createdAt);
    }

    // -----------------------------
    // Person/User Registration Email
    // -----------------------------
    public String generatePersonRegistrationSuccessEmail(
            String name,
            String userId,
            String email,
            String orgName,
            String orgId,
            String category,
            LocalDateTime createdAt
    ) {
        String registeredAt = createdAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">Administrator Registration Successful</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your account under the organization <strong>%s</strong> has been successfully created.</p>
                <div style="padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    <p><strong>User ID:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Registered On:</strong> %s</p>
                    <p><strong>Organization ID:</strong> %s</p>
                    <p><strong>Organization Name:</strong> %s</p>
                    <p><strong>Category:</strong> %s</p>
                </div>
                <p>You can now log in using your <strong>User ID</strong> or registered email on the Medic-connect Dashboard.</p>
                <p>Welcome aboard and thank you for joining Medic-connect!</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890<br>
                    &copy; 2025 Medic-connect. All rights reserved.
                </p>
            </div>
        </body>
        </html>
        """.formatted(name, orgName, userId, email, registeredAt, orgId, orgName, category);
    }

    // -----------------------------
    // Login OTP Email
    // -----------------------------
    public String generateLoginOtpEmail(String name, String otp) {
        String issuedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">Medic-connect Login OTP</h2>
                <p>Hello <strong>%s</strong>,</p>
                <p>Your one-time password (OTP) for login is:</p>
                <h3 style="color:#000;">%s</h3>
                <p>This OTP is valid for 10 minutes only.</p>
                <p>Issued on: %s</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890<br>
                    &copy; 2025 Medic-connect. All rights reserved.<br>
                    Do not share this OTP with anyone.
                </p>
            </div>
        </body>
        </html>
        """.formatted(name, otp, issuedAt);
    }
}
