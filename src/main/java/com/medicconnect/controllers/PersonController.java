package com.medicconnect.controllers;

import com.medicconnect.models.Person;
import com.medicconnect.models.Organization;
import com.medicconnect.repositories.OrganizationRepository;
import com.medicconnect.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private OrganizationRepository organizationRepository;

    // -----------------------------
    // Get all persons
    // -----------------------------
    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    // -----------------------------
    // Get person by ID
    // -----------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        return personService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // -----------------------------
    // Create a new person linked to an organization
    // -----------------------------
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        if (person.getOrganization() != null && person.getOrganization().getId() != null) {
            Organization org = organizationRepository.findById(person.getOrganization().getId()).orElse(null);
            if (org == null) return ResponseEntity.badRequest().build();
            person.setOrganization(org);
        }

        // Force role to ADMIN for org registration
        person.setRole("ADMIN");

        Person savedPerson = personService.createPerson(person);
        return ResponseEntity.ok(savedPerson);
    }

    // -----------------------------
    // Update a person
    // -----------------------------
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person updatedPerson) {
        return personService.findById(id).map(person -> {
            person.setName(updatedPerson.getName());
            person.setDob(updatedPerson.getDob());
            person.setGender(updatedPerson.getGender());
            person.setBloodGroup(updatedPerson.getBloodGroup());
            person.setMobile(updatedPerson.getMobile());
            person.setEmail(updatedPerson.getEmail());

            if (updatedPerson.getOrganization() != null && updatedPerson.getOrganization().getId() != null) {
                Organization org = organizationRepository.findById(updatedPerson.getOrganization().getId()).orElse(null);
                person.setOrganization(org);
            }

            personService.savePerson(person);
            return ResponseEntity.ok(person);
        }).orElse(ResponseEntity.notFound().build());
    }

    // -----------------------------
    // Delete a person
    // -----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        return personService.findById(id).map(person -> {
            personService.deletePerson(person);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
