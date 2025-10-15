package com.medicconnect.services;

import com.medicconnect.models.Person;
import com.medicconnect.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Login with email or userId and password
    public boolean login(String identifier, String password) {
        Optional<Person> optionalPerson = personRepository.findByEmail(identifier);
        if(optionalPerson.isEmpty()){
            optionalPerson = personRepository.findByUserId(identifier);
        }

        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            return passwordEncoder.matches(password, person.getPassword());
        }

        return false;
    }

    // Change password
    public boolean changePassword(String userId, String newPassword) {
        Optional<Person> optionalPerson = personRepository.findByUserId(userId);
        if(optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setPassword(passwordEncoder.encode(newPassword));
            personRepository.save(person);
            return true;
        }
        return false;
    }
}
