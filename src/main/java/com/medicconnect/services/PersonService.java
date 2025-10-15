package com.medicconnect.services;

import com.medicconnect.models.Person;
import com.medicconnect.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // -----------------------------
    // Create a new person/user
    // -----------------------------
    public Person createPerson(Person person) {
        if (person.getEmail() != null && person.getPhoneNumber() != null) {

            // Ensure role is set
            if (person.getRole() == null) {
                person.setRole("ADMIN");
            }

            // Generate unique userId safely
            String userId = idGeneratorService.generateUserId(
                    person.getRole(),
                    person.getOrganization() != null ? person.getOrganization().getId().toString() : null
            );
            person.setUserId(userId);

            // Encode password if provided
            if (person.getPassword() != null) {
                person.setPassword(passwordEncoder.encode(person.getPassword()));
            }
        }
        return personRepository.save(person);
    }

    // -----------------------------
    // Get all persons
    // -----------------------------
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // -----------------------------
    // Find person by ID
    // -----------------------------
    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    // -----------------------------
    // Save/Update person
    // -----------------------------
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    // -----------------------------
    // Delete person
    // -----------------------------
    public void deletePerson(Person person) {
        personRepository.delete(person);
    }

    // -----------------------------
    // Find person by userId
    // -----------------------------
    public Optional<Person> findByUserId(String userId) {
        return personRepository.findByUserId(userId);
    }

    // -----------------------------
    // Change password
    // -----------------------------
    public boolean changePassword(String userId, String newPassword) {
        Optional<Person> optionalPerson = findByUserId(userId);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(person);
            return true;
        }
        return false;
    }
}
