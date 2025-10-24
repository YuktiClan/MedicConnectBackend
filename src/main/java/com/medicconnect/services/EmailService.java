package com.medicconnect.services;

import com.medicconnect.dto.PersonDTO;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${spring.mail.username}")
    private String emailUser;

    @Value("${spring.mail.password}")
    private String emailPass;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a");

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
            protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
                return new jakarta.mail.PasswordAuthentication(emailUser, emailPass);
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
        String createdAt = LocalDateTime.now().format(DATE_FORMATTER);
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
    // Main Admin Registration Email
    // -----------------------------
    public String generatePersonRegistrationSuccessEmail(String name, String userId, String email,
                                                         String orgName, String orgId, String category, LocalDateTime createdAt) {
        String registeredAt = createdAt.format(DATE_FORMATTER);
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
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890
                </p>
            </div>
        </body>
        </html>
        """.formatted(name, orgName, userId, email, registeredAt, orgId, orgName, category);
    }

    // -----------------------------
    // Pre-Filled Registration Link Email
    // -----------------------------
    public String generatePreFilledRegistrationEmail(PersonDTO dto, String registrationLink) {
        String roleList = String.join(", ", dto.getRoles());
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">Complete Your Registration</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>You have been invited to register an account under your organization. Your basic details have been pre-filled.</p>
                <div style="padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    <p><strong>Name:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Mobile:</strong> %s</p>
                    <p><strong>Role(s):</strong> %s</p>
                    <p><strong>Organization ID:</strong> %s</p>
                </div>
                <p>Please click the link below to complete your registration and set your password:</p>
                <p><a href="%s" style="color:#0077ff; text-decoration:none;">Complete Registration</a></p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    If you did not expect this email, please ignore it.<br>
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(dto.getName(), dto.getName(), dto.getEmail(), dto.getMobile(), roleList, dto.getOrgId(), registrationLink);
    }

    // -----------------------------
    // Notify Main Admin: New User Created
    // -----------------------------
    public String generateNewUserNotificationForAdmin(String adminName, String newUserName, String newUserEmail,
                                                       String newUserId, List<String> roles, String orgName,
                                                       String orgId, String category, LocalDateTime createdAt) {
        String createdAtStr = createdAt.format(DATE_FORMATTER);
        String roleList = String.join(", ", roles);
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">New User Registration Pending Approval</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>A new user has registered under your organization <strong>%s</strong> and requires your approval.</p>
                <div style="padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    <p><strong>User Name:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>User ID:</strong> %s</p>
                    <p><strong>Role(s):</strong> %s</p>
                    <p><strong>Organization ID:</strong> %s</p>
                    <p><strong>Organization Name:</strong> %s</p>
                    <p><strong>Category:</strong> %s</p>
                    <p><strong>Registered On:</strong> %s</p>
                </div>
                <p>Please check the Medic-connect Dashboard to <strong>approve or deny</strong> this user.</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890
                </p>
            </div>
        </body>
        </html>
        """.formatted(adminName, orgName, newUserName, newUserEmail, newUserId, roleList, orgId, orgName, category, createdAtStr);
    }

    // -----------------------------
    // Notify User: Pending Approval
    // -----------------------------
    public String generateUserRegistrationPendingEmail(String userName, String userId, String email, List<String> roles,
                                                        String orgName, String orgId, String category, LocalDateTime createdAt) {
        String registeredAt = createdAt.format(DATE_FORMATTER);
        String roleList = String.join(", ", roles);
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">User Registration Successful (Pending Approval)</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your account under the organization <strong>%s</strong> has been successfully registered but is <strong>pending approval</strong> by the main admin.</p>
                <div style="padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    <p><strong>User ID:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Role(s):</strong> %s</p>
                    <p><strong>Organization ID:</strong> %s</p>
                    <p><strong>Organization Name:</strong> %s</p>
                    <p><strong>Category:</strong> %s</p>
                    <p><strong>Registered On:</strong> %s</p>
                </div>
                <p>You will be notified once your account is approved. You cannot log in until the main admin approves your account.</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890
                </p>
            </div>
        </body>
        </html>
        """.formatted(userName, orgName, userId, email, roleList, orgId, orgName, category, registeredAt);
    }

    // -----------------------------
    // Notify User: Approved
    // -----------------------------
    public String generateUserApprovedEmail(String userName, String userId, String email, List<String> roles,
                                            String orgName, String orgId, String category, LocalDateTime approvedAt) {
        String approvedTime = approvedAt.format(DATE_FORMATTER);
        String roleList = String.join(", ", roles);
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">User Account Approved</h2>
                <p>Dear <strong>%s</strong>,</p>
                <p>Your account under the organization <strong>%s</strong> has been approved by the main admin. You can now log in to the Medic-connect Dashboard.</p>
                <div style="padding:12px; background:#e8f3ff; border-left:5px solid #0077ff; border-radius:6px;">
                    <p><strong>User ID:</strong> %s</p>
                    <p><strong>Email:</strong> %s</p>
                    <p><strong>Role(s):</strong> %s</p>
                    <p><strong>Organization ID:</strong> %s</p>
                    <p><strong>Organization Name:</strong> %s</p>
                    <p><strong>Category:</strong> %s</p>
                    <p><strong>Approved On:</strong> %s</p>
                </div>
                <p>You can now log in using your <strong>User ID</strong> or registered email.</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a> or call +91-1234567890
                </p>
            </div>
        </body>
        </html>
        """.formatted(userName, orgName, userId, email, roleList, orgId, orgName, category, approvedTime);
    }

    // -----------------------------
    // Login / Verification OTP Email
    // -----------------------------
    public String generateOtpEmail(String name, String otp, String purpose) {
        String issuedAt = LocalDateTime.now().format(DATE_FORMATTER);
        return """
        <html>
        <body style="font-family:Arial,sans-serif; background:#f4f8fb; padding:20px;">
            <div style="max-width:600px; margin:auto; background:#fff; padding:25px; border-radius:12px;">
                <h2 style="color:#0077ff;">Medic-connect %s OTP</h2>
                <p>Hello <strong>%s</strong>,</p>
                <p>Your one-time password (OTP) is:</p>
                <h3 style="color:#000;">%s</h3>
                <p>This OTP is valid for 10 minutes only.</p>
                <p>Issued on: %s</p>
                <hr>
                <p style="font-size:12px; color:#555;">
                    Do not share this OTP with anyone. If you did not request this, please ignore this email.<br>
                    Need assistance? Contact <a href="mailto:support@medic-connect.example">support@medic-connect.example</a>
                </p>
            </div>
        </body>
        </html>
        """.formatted(purpose, name, otp, issuedAt);
    }

    // -----------------------------
// Rejected User Notification
// -----------------------------
public String generateUserRejectedEmail(
        String userName,
        String userId,
        String email,
        List<String> roles,
        String orgName,
        String orgId,
        String category,
        LocalDateTime rejectedAt
) {
    String rejectedTime = rejectedAt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    String roleList = String.join(", ", roles);
    return """
        <html>
        <body>
            <p>Dear %s,</p>
            <p>Your registration under organization %s has been rejected by the main admin.</p>
            <p>User ID: %s<br>Email: %s<br>Roles: %s<br>Organization: %s (%s)<br>Category: %s<br>Rejected On: %s</p>
            <p>Please contact your organization admin for details.</p>
        </body>
        </html>
        """.formatted(userName, orgName, userId, email, roleList, orgName, orgId, category, rejectedTime);
}

// -----------------------------
// Login OTP Email
// -----------------------------
public String generateLoginOtpEmail(String name, String otp) {
    String issuedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    return "<p>Hello " + name + ", your OTP is " + otp + ". Issued at: " + issuedAt + "</p>";
}

// -----------------------------
// Email Verification OTP
// -----------------------------
public String generateEmailVerificationOtpEmail(String name, String otp) {
    String issuedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a"));
    return "<p>Hello " + name + ", your verification OTP is " + otp + ". Issued at: " + issuedAt + "</p>";
}

}
