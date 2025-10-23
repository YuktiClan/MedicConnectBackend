package com.medicconnect.controllers;

import com.medicconnect.models.Person;
import com.medicconnect.services.PersonService;
import com.medicconnect.services.VerificationService;
import com.medicconnect.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String password = request.get("password");

        if (identifier == null || identifier.isBlank() || password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error("Identifier and password are required", null));
        }

        try {
            Person person = personService.findByEmailOrUserId(identifier)
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with email or ID: " + identifier
                    ));

            if (!passwordEncoder.matches(password, person.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(ResponseUtils.error("Invalid credentials", null));
            }

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("userId", person.getUserId());
            userInfo.put("name", person.getName());
            userInfo.put("email", person.getEmail());
            userInfo.put("mobile", person.getMobile());
            userInfo.put("organizationId", person.getOrganization() != null ? person.getOrganization().getOrgId() : null);

            return ResponseEntity.ok(ResponseUtils.success("Login successful", userInfo));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error(e.getMessage(), null));
        }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");

        if (identifier == null || identifier.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error("Identifier is required", null));
        }

        try {
            Person person = personService.findByEmailOrUserId(identifier)
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with email or ID: " + identifier
                    ));

            verificationService.sendOtp(person.getEmail(), "email");

            return ResponseEntity.ok(ResponseUtils.success(
                    "OTP sent successfully",
                    Map.of("email", person.getEmail())
            ));

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error(e.getMessage(), null));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody Map<String, String> request) {
        String identifier = request.get("identifier");
        String otp = request.get("otp");

        if (identifier == null || identifier.isBlank() || otp == null || otp.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error("Identifier and OTP are required", null));
        }

        try {
            Person person = personService.findByEmailOrUserId(identifier)
                    .orElseThrow(() -> new RuntimeException(
                            "User not found with email or ID: " + identifier
                    ));

            boolean verified = verificationService.verifyOtp(person.getEmail(), otp);

            if (!verified) {
                return ResponseEntity.badRequest()
                        .body(ResponseUtils.error("Invalid OTP", null));
            }

            Map<String, Object> userInfo = Map.of(
                    "userId", person.getUserId(),
                    "email", person.getEmail(),
                    "name", person.getName()
            );

            return ResponseEntity.ok(ResponseUtils.success("OTP verified successfully", userInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ResponseUtils.error(e.getMessage(), null));
        }
    }
}
