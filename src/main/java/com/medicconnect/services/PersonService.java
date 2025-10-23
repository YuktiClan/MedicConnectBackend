package com.medicconnect.services;

import com.medicconnect.dto.PersonDTO;
import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.repositories.OrganizationRepository;
import com.medicconnect.repositories.PersonRepository;
import com.medicconnect.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final OrganizationRepository organizationRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PersonService(PersonRepository personRepository,
                         OrganizationRepository organizationRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.organizationRepository = organizationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Person createPerson(PersonDTO dto) {
        Organization org = organizationRepository.findByOrgId(dto.getOrgId())
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        Person person = dto.toPerson(org);

        if (person.getPassword() != null && !person.getPassword().startsWith("$2a$")) {
            person.setPassword(passwordEncoder.encode(person.getPassword()));
        }

        ValidationUtils.validateEmail(person.getEmail());
        return personRepository.save(person);
    }

    public Person findById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    public Person updatePerson(Long id, PersonDTO dto, Organization org) {
        Person person = findById(id);
        if (dto.getName() != null) person.setName(dto.getName());
        if (dto.getEmail() != null) person.setEmail(dto.getEmail());
        if (dto.getMobile() != null) person.setMobile(dto.getMobile());
        if (dto.getGender() != null) person.setGender(dto.getGender());
        if (dto.getBloodGroup() != null) person.setBloodGroup(dto.getBloodGroup());
        if (org != null) person.setOrganization(org);
        return personRepository.save(person);
    }

    public Optional<Person> findByEmailOrUserId(String emailOrUserId) {
        return personRepository.findByEmailOrUserId(emailOrUserId);
    }
}
