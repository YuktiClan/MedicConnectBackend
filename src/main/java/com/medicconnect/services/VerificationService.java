package com.medicconnect.services;

import com.medicconnect.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private final Random random = new Random();
    private final EmailService emailService;

    @Autowired
    public VerificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    // -----------------------------
    // Send OTP via email or phone
    // -----------------------------
    public void sendOtp(String target, String type) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otpStore.put(target, otp);

        if ("email".equalsIgnoreCase(type)) {
            try {
                // Use EmailService for sending OTP email
                String htmlBody = emailService.generateLoginOtpEmail(target, otp);
                emailService.sendEmail(target, "Medic-connect OTP Verification", htmlBody);
                System.out.println("[VerificationService] Email OTP sent to " + target);
            } catch (Exception e) {
                System.err.println("[VerificationService] Failed to send OTP email to " + target + ": " + e.getMessage());
            }
        } else {
            // For phone, just log (can integrate SMS later)
            System.out.println("[VerificationService] Phone OTP for " + target + ": " + otp);
        }
    }

    // -----------------------------
    // Verify OTP
    // -----------------------------
    public boolean verifyOtp(String target, String otp) {
        String correctOtp = otpStore.get(target);
        if (correctOtp != null && correctOtp.equals(otp)) {
            otpStore.remove(target);
            return true;
        }
        return false;
    }
}
