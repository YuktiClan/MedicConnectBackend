package com.medicconnect.controllers;

import com.medicconnect.dto.PersonDTO;
import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.services.EmailService;
import com.medicconnect.services.OrganizationService;
import com.medicconnect.services.PersonService;
import com.medicconnect.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;
    private final OrganizationService organizationService;
    private final EmailService emailService;

    public PersonController(PersonService personService,
                            OrganizationService organizationService,
                            EmailService emailService) {
        this.personService = personService;
        this.organizationService = organizationService;
        this.emailService = emailService;
    }

    // -----------------------------
    // Create Main Admin
    // -----------------------------
    @PostMapping("/main-admin")
    public ResponseEntity<?> createMainAdmin(@RequestBody PersonDTO dto) {
        try {
            Organization org = validateOrganization(dto.getOrgId());
            Person savedAdmin = personService.createMainAdmin(dto);
            sendEmail(savedAdmin, org, "Medic-connect | Registration Successful",
                    emailService.generatePersonRegistrationSuccessEmail(
                            savedAdmin.getName(),
                            savedAdmin.getUserId(),
                            savedAdmin.getEmail(),
                            org.getOrganizationName(),
                            org.getOrgId(),
                            org.getCategory(),
                            savedAdmin.getAssociatedDate()
                    ));

            return ResponseEntity.ok(ResponseUtils.success(
                    "Main Admin created successfully",
                    Map.of("userId", savedAdmin.getUserId(), "orgId", org.getOrgId())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to create main admin", e.getMessage()));
        }
    }

    // -----------------------------
    // Register Role-Type User
    // -----------------------------
    @PostMapping
    public ResponseEntity<?> registerRoleUser(@RequestBody PersonDTO dto) {
        try {
            Organization org = validateOrganization(dto.getOrgId());
            Person savedUser = personService.createUser(dto);

            notifyMainAdmin(savedUser);
            notifyUserPending(savedUser);

            return ResponseEntity.ok(ResponseUtils.success(
                    "User registered successfully; pending main admin approval",
                    Map.of("userId", savedUser.getUserId(), "status", savedUser.getStatus())
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to register user", e.getMessage()));
        }
    }

    // -----------------------------
    // Get Main Admin by Organization ID
    // -----------------------------
    @GetMapping("/main-admin")
    public ResponseEntity<?> getMainAdminByOrgId(@RequestParam String orgId) {
        try {
            Optional<Person> mainAdminOpt = personService.getAllPersons().stream()
                    .filter(p -> p.getOrganization() != null
                            && orgId.equals(p.getOrganization().getOrgId())
                            && p.getRoles() != null
                            && p.getRoles().contains("MAIN_ADMIN"))
                    .findFirst();

            if (mainAdminOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(ResponseUtils.error("Main Admin not found for orgId: " + orgId, null));
            }

            return ResponseEntity.ok(ResponseUtils.success("Main Admin fetched successfully", mainAdminOpt.get()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to fetch main admin", e.getMessage()));
        }
    }

    // -----------------------------
    // Get All Persons
    // -----------------------------
    @GetMapping
    public ResponseEntity<?> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(ResponseUtils.success("Persons fetched successfully", persons));
    }

    // -----------------------------
    // Get Person by ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        try {
            Person person = personService.findById(id);
            return ResponseEntity.ok(ResponseUtils.success("Person fetched successfully", person));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to fetch person", e.getMessage()));
        }
    }

    // GET /api/persons/pending?orgId={orgId}
@GetMapping("/pending")
public ResponseEntity<?> getPendingUsers(@RequestParam String orgId) {
    try {
        List<Person> pendingUsers = personService.getPendingUsersByOrg(orgId);
        return ResponseEntity.ok(ResponseUtils.success("Pending users fetched successfully", pendingUsers));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to fetch pending users", e.getMessage()));
    }
}


// POST /api/persons/approve/{userId}
@PostMapping("/approve/{userId}")
public ResponseEntity<?> approveUser(@PathVariable Long userId) {
    try {
        Person approvedUser = personService.approveUser(userId);
        return ResponseEntity.ok(ResponseUtils.success(
                "User approved successfully",
                Map.of("userId", approvedUser.getUserId(), "status", approvedUser.getStatus())
        ));
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to approve user", e.getMessage()));
    }
}


    // -----------------------------
    // Update Person
    // -----------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody PersonDTO dto) {
        try {
            Organization org = dto.getOrgId() != null ? organizationService.findByOrgId(dto.getOrgId()) : null;
            Person updated = personService.updatePerson(id, dto, org);
            return ResponseEntity.ok(ResponseUtils.success("Person updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to update person", e.getMessage()));
        }
    }

    // -----------------------------
    // Delete Person
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {
            personService.deleteById(id);
            return ResponseEntity.ok(ResponseUtils.success("Person deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error("Failed to delete person", e.getMessage()));
        }
    }

    // -----------------------------
    // Helper Methods
    // -----------------------------
    private Organization validateOrganization(String orgId) {
        if (orgId == null || orgId.isBlank()) {
            throw new RuntimeException("Organization ID is required");
        }
        Organization org = organizationService.findByOrgId(orgId);
        if (org == null) {
            throw new RuntimeException("Organization not found");
        }
        return org;
    }

    private void sendEmail(Person person, Organization org, String subject, String body) {
        if (person.getEmail() != null) {
            try {
                emailService.sendEmail(person.getEmail(), subject, body);
            } catch (Exception e) {
                System.err.println("[EmailService] Failed to send email: " + e.getMessage());
            }
        }
    }

    private void notifyMainAdmin(Person user) {
        Optional<Person> mainAdminOpt = personService.getAllPersons().stream()
                .filter(p -> p.getOrganization() != null
                        && user.getOrganization() != null
                        && p.getOrganization().getOrgId().equals(user.getOrganization().getOrgId())
                        && p.getRoles() != null
                        && p.getRoles().contains("MAIN_ADMIN"))
                .findFirst();

        mainAdminOpt.ifPresent(admin -> sendEmail(admin, user.getOrganization(),
                "Medic-connect | New User Pending Approval",
                emailService.generateNewUserNotificationForAdmin(
                        admin.getName(),
                        user.getName(),
                        user.getEmail(),
                        user.getUserId(),
                        user.getRoles(),
                        user.getOrganization().getOrganizationName(),
                        user.getOrganization().getOrgId(),
                        user.getOrganization().getCategory(),
                        user.getAssociatedDate()
                )));
    }

    private void notifyUserPending(Person user) {
        sendEmail(user, user.getOrganization(),
                "Medic-connect | Registration Pending Approval",
                emailService.generateUserRegistrationPendingEmail(
                        user.getName(),
                        user.getUserId(),
                        user.getEmail(),
                        user.getRoles(),
                        user.getOrganization().getOrganizationName(),
                        user.getOrganization().getOrgId(),
                        user.getOrganization().getCategory(),
                        user.getAssociatedDate()
                ));
    }
}
