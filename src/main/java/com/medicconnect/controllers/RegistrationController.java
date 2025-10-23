package com.medicconnect.controllers;

import com.medicconnect.dto.PersonDTO;
import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.services.EmailService;
import com.medicconnect.services.FieldService;
import com.medicconnect.services.OrganizationService;
import com.medicconnect.services.PersonService;
import com.medicconnect.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final FieldService fieldService;
    private final OrganizationService organizationService;
    private final PersonService personService;
    private final EmailService emailService;

    public RegistrationController(FieldService fieldService,
                                  OrganizationService organizationService,
                                  PersonService personService,
                                  EmailService emailService) {
        this.fieldService = fieldService;
        this.organizationService = organizationService;
        this.personService = personService;
        this.emailService = emailService;
    }

    @GetMapping("/form")
    public ResponseEntity<Map<String, Object>> getForm() {
        return ResponseEntity.ok(ResponseUtils.success(
                "Registration form fetched successfully",
                fieldService.getHospitalRegistrationForm()
        ));
    }

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitRegistration(@RequestBody Map<String, Object> formData) {
        try {
            Map<String, Object> orgData = (Map<String, Object>) formData.get("organization");
            String orgName = (String) orgData.get("organizationName");

            Organization org = organizationService.findByOrganizationNameOptional(orgName)
                    .orElseGet(() -> organizationService.createOrganizationFromMap(orgData));

            Map<String, Object> personalData = (Map<String, Object>) formData.get("personal");
            Map<String, Object> authData = (Map<String, Object>) formData.get("auth");

            PersonDTO personDTO = new PersonDTO();
            personDTO.setName((String) personalData.get("name"));
            personDTO.setEmail((String) personalData.get("email"));
            personDTO.setMobile((String) personalData.get("mobile"));
            personDTO.setDob(personalData.get("dob") != null ? LocalDate.parse(personalData.get("dob").toString()) : null);
            personDTO.setGender((String) personalData.get("gender"));
            personDTO.setBloodGroup((String) personalData.get("bloodGroup"));
            personDTO.setPassword((String) authData.get("password"));
            personDTO.setAgreement((Boolean) authData.get("agreement"));
            personDTO.setOrgId(org.getOrgId());

            Person savedAdmin = personService.createPerson(personDTO);

            if (savedAdmin.getEmail() != null) {
                String htmlBody = emailService.generatePersonRegistrationSuccessEmail(
                        savedAdmin.getName(),
                        savedAdmin.getUserId(),
                        savedAdmin.getEmail(),
                        org.getOrganizationName(),
                        org.getOrgId(),
                        org.getCategory(),
                        LocalDateTime.now()
                );
                emailService.sendEmail(savedAdmin.getEmail(),
                        "Medic-connect | Registration Successful", htmlBody);
            }

            return ResponseEntity.ok(ResponseUtils.success(
                    "Organization and Admin registered successfully",
                    Map.of(
                            "organizationId", org.getOrgId(),
                            "adminUserId", savedAdmin.getUserId()
                    )
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ResponseUtils.error(
                    "Failed to register organization or admin", e.getMessage()
            ));
        }
    }
}
