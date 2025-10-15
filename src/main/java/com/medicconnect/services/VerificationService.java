package com.medicconnect.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationService {

    private final Map<String, String> otpStore = new ConcurrentHashMap<>(); // email/phone -> OTP
    private final Random random = new Random();

    private final EmailService emailService;

    @Autowired
    public VerificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Generate OTP and send for email or phone.
     * @param target email or phone number
     * @param type "email" or "phone"
     */
    public void sendOtp(String target, String type) {
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otpStore.put(target, otp);

        if ("email".equalsIgnoreCase(type)) {
            try {
                String htmlBody = emailService.generateOtpEmailBody(target, otp);
                emailService.sendEmail(target, "Medic-connect OTP Verification", htmlBody);
                System.out.println("[VerificationService] Email OTP sent to " + target);
            } catch (Exception e) {
                System.err.println("[VerificationService] Failed to send OTP email to " + target + ": " + e.getMessage());
            }
        } else if ("phone".equalsIgnoreCase(type)) {
            // Phone OTP for dev/testing (console only)
            System.out.println("[VerificationService] Phone OTP for " + target + ": " + otp);
        }
    }

    /**
     * Verify OTP entered by user.
     * @param target email or phone
     * @param otp OTP entered by user
     * @return true if OTP is correct, false otherwise
     */
    public boolean verifyOtp(String target, String otp) {
        String correctOtp = otpStore.get(target);
        if (correctOtp != null && correctOtp.equals(otp)) {
            otpStore.remove(target); // Remove after successful verification
            return true;
        }
        return false;
    }
}
