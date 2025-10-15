package com.medicconnect.controllers;

import com.medicconnect.models.FormBlock;
import com.medicconnect.services.FieldService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms") // plural is more REST-consistent
public class FormController {

    private final FieldService fieldService;

    public FormController(FieldService fieldService) {
        this.fieldService = fieldService;
    }

    /**
     * Returns the dynamic Hospital Registration Form configuration.
     */
    @GetMapping("/hospital-registration")
    public ResponseEntity<List<FormBlock>> getHospitalRegistrationForm() {
        List<FormBlock> blocks = fieldService.getHospitalRegistrationForm();
        return ResponseEntity.ok(blocks);
    }
}
