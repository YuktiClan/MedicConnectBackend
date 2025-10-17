package com.medicconnect.controllers;

import com.medicconnect.models.Person;
import com.medicconnect.services.PersonService;
import com.medicconnect.services.VerificationService;
import com.medicconnect.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final PersonService personService;
    private final VerificationService verificationService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(PersonService personService,
                          VerificationService verificationService,
                          BCryptPasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.verificationService = verificationService;
        this.passwordEncoder = passwordEncoder;
    }

    // ------------------- PASSWORD LOGIN -------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String password = request.get("password");

        if (identifier == null || password == null) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error("Identifier and password are required", null));
        }

        try {
            Person person = personService.findByEmailOrUserId(identifier);
            if (!passwordEncoder.matches(password, person.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(ResponseUtils.error("Invalid password", null));
            }
            return ResponseEntity.ok(ResponseUtils.success("Login successful", person));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("User not found", null));
        }
    }

    // ------------------- SEND OTP -------------------
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");

        if (identifier == null) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error("Identifier is required", null));
        }

        try {
            Person person = personService.findByEmailOrUserId(identifier);
            verificationService.sendOtp(person.getEmail(), "email");
            return ResponseEntity.ok(ResponseUtils.success("OTP sent successfully", Map.of("email", person.getEmail())));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("User not found", null));
        }
    }

    // ------------------- VERIFY OTP -------------------
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String otp = request.get("otp");

        if (identifier == null || otp == null) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error("Identifier and OTP are required", null));
        }

        try {
            Person person = personService.findByEmailOrUserId(identifier);
            boolean verified = verificationService.verifyOtp(person.getEmail(), otp);
            if (!verified) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Invalid OTP", null));
            }
            return ResponseEntity.ok(ResponseUtils.success("OTP verified successfully", person));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("User not found", null));
        }
    }
}
