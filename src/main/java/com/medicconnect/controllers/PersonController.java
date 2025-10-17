package com.medicconnect.controllers;

import com.medicconnect.dto.PersonDTO;
import com.medicconnect.models.Person;
import com.medicconnect.services.EmailService;
import com.medicconnect.services.PersonService;
import com.medicconnect.utils.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;       // for DOB
import java.time.LocalDateTime;   // for registrationDate, associatedDate

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonService personService;
    private final EmailService emailService;

    public PersonController(PersonService personService, EmailService emailService) {
        this.personService = personService;
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
            Person person = personService.createPerson(dto.toPerson());

            if (person.getEmail() != null) {
                String orgName = person.getOrganization() != null ? person.getOrganization().getOrganizationName() : "N/A";
                String htmlBody = emailService.generatePersonRegistrationSuccessEmail(
                        person.getName(), person.getRole(), person.getUserId(),
                        person.getEmail(), orgName, new Date()
                );
                emailService.sendEmail(person.getEmail(),
                        "Medic-connect | Registration Successful",
                        htmlBody
                );
            }

            return ResponseEntity.ok(ResponseUtils.success("Person registered successfully", person));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseUtils.error(e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody PersonDTO dto) {
        try {
            Person updated = personService.updatePerson(id, dto);
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
