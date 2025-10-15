package com.medicconnect.controllers;

import com.medicconnect.models.FormBlock;
import com.medicconnect.services.FieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final FieldService fieldService;

    public RegistrationController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    /**
     * Submit form data for registration (hospital/admin/etc.)
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitRegistration(@RequestBody Map<String, Object> formData) {
        // Here you can save the form data to DB
        // For now, just return success
        System.out.println("Received registration data: " + formData);
        return ResponseEntity.ok(Map.of("message", "Registration submitted successfully"));
    }

    /**
     * Get hospital registration form metadata
     */
    @GetMapping("/form")
    public ResponseEntity<List<FormBlock>> getForm() {
        List<FormBlock> blocks = fieldService.getHospitalRegistrationForm();
        return ResponseEntity.ok(blocks);
    }
}
