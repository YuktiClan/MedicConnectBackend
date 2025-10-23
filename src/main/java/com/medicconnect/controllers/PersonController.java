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

import java.time.LocalDateTime;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<?> getAllPersons() {
        List<Person> persons = personService.getAllPersons();
        return ResponseEntity.ok(ResponseUtils.success("Persons fetched successfully", persons));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable Long id) {
        try {
            Person person = personService.findById(id);
            return ResponseEntity.ok(ResponseUtils.success("Person fetched successfully", person));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<?> createPerson(@RequestBody PersonDTO dto) {
        try {
            Organization org = organizationService.findByOrgId(dto.getOrgId());
            Person savedPerson = personService.createPerson(dto);

            if (savedPerson.getEmail() != null) {
                String htmlBody = emailService.generatePersonRegistrationSuccessEmail(
                        savedPerson.getName(),
                        savedPerson.getUserId(),
                        savedPerson.getEmail(),
                        org.getOrganizationName(),
                        org.getOrgId(),
                        org.getCategory(),
                        LocalDateTime.now()
                );
                emailService.sendEmail(savedPerson.getEmail(),
                        "Medic-connect | Registration Successful", htmlBody);
            }

            return ResponseEntity.ok(ResponseUtils.success("Person registered successfully", savedPerson));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody PersonDTO dto) {
        try {
            Organization org = null;
            if (dto.getOrgId() != null) org = organizationService.findByOrgId(dto.getOrgId());
            Person updated = personService.updatePerson(id, dto, org);
            return ResponseEntity.ok(ResponseUtils.success("Person updated successfully", updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {
            personService.deleteById(id);
            return ResponseEntity.ok(ResponseUtils.success("Person deleted successfully", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage(), null));
        }
    }
}
