package com.medicconnect.controllers;

import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.services.FieldService;
import com.medicconnect.services.OrganizationService;
import com.medicconnect.services.PersonService;
import com.medicconnect.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final FieldService fieldService;
    private final OrganizationService organizationService;
    private final PersonService personService;

    public RegistrationController(FieldService fieldService,
                                  OrganizationService organizationService,
                                  PersonService personService) {
        this.fieldService = fieldService;
        this.organizationService = organizationService;
        this.personService = personService;
    }

    // ------------------- GET REGISTRATION FORM -------------------
    @GetMapping("/form")
    public ResponseEntity<Map<String, Object>> getForm() {
        return ResponseEntity.ok(ResponseUtils.success("Registration form fetched successfully",
                fieldService.getHospitalRegistrationForm()));
    }

    // ------------------- SUBMIT REGISTRATION -------------------
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitRegistration(@RequestBody Map<String, Object> formData) {
    try {
        Map<String, Object> orgData = (Map<String, Object>) formData.get("organization");
        Organization org = organizationService.createOrganizationFromMap(orgData);

        Map<String, Object> personalData = (Map<String, Object>) formData.get("personal");
        Map<String, Object> authData = (Map<String, Object>) formData.get("auth");
        Person admin = personService.createPersonFromMap(personalData, authData, org);

        return ResponseEntity.ok(ResponseUtils.success(
                "Organization and Admin registered successfully",
                Map.of("organizationId", org.getOrgId(), "adminUserId", admin.getUserId())
        ));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(ResponseUtils.error(
                "Failed to register organization or admin", e.getMessage()
        ));
    }
}

}
