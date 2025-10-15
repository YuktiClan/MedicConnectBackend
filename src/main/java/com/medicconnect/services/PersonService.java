package com.medicconnect.services;

import com.medicconnect.models.Person;
import com.medicconnect.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Create a new person/user
    public Person createPerson(Person person) {

        // Only generate userId if personal email and phone are present
        if(person.getPersonalEmail() != null && person.getPhoneNumber() != null) {
            String userId = idGeneratorService.generateUserId(person.getRole(), person.getOrgId());
            person.setUserId(userId);

            // Encode password
            if(person.getPassword() != null) {
                person.setPassword(passwordEncoder.encode(person.getPassword()));
            }
        }

        return personRepository.save(person);
    }

    // Find by userId
    public Optional<Person> findByUserId(String userId) {
        return personRepository.findByUserId(userId);
    }

    // Change password
    public boolean changePassword(String userId, String newPassword) {
        Optional<Person> optionalPerson = findByUserId(userId);
        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(person);
            return true;
        }
        return false;
    }
}
