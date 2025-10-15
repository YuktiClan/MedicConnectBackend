package com.medicconnect.controllers;

import com.medicconnect.services.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/verify")
public class VerificationController {

    private final VerificationService verificationService;

    public VerificationController(VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    // Send OTP
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> payload) {
        String target = payload.get("target"); // email or phone
        String type = payload.get("type");     // "email" or "phone"

        if (target == null || type == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing target or type"));
        }

        verificationService.sendOtp(target, type);
        return ResponseEntity.ok(Map.of("message", "OTP sent successfully"));
    }

    // Verify OTP
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        String target = payload.get("target");
        String otp = payload.get("otp");

        if (target == null || otp == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Missing target or OTP"));
        }

        boolean verified = verificationService.verifyOtp(target, otp);
        if (verified) {
            return ResponseEntity.ok(Map.of("message", "Verified successfully"));
        } else {
            return ResponseEntity.status(400).body(Map.of("error", "Invalid OTP"));
        }
    }
}
