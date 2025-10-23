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
import java.time.LocalDate;       // for DOB
import java.time.LocalDateTime;   // for registrationDate, associatedDate



import java.util.List;
import java.util.Map;
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
    public Person createPersonFromMap(Map<String, Object> personalData,
                                      Map<String, Object> authData,
                                      Organization org) {
        Person person = new Person();

        person.setName((String) personalData.get("name"));
        person.setEmail((String) personalData.get("email"));

        String mobile = (String) personalData.get("mobile");
        if (mobile != null) mobile = mobile.replaceAll("[^0-9+]", "");
        person.setMobile(mobile);

        person.setGender((String) personalData.get("gender"));
        person.setBloodGroup((String) personalData.get("bloodGroup"));

        Object dobObj = personalData.get("dob");
        if (dobObj != null) {
            String dobString = dobObj.toString().substring(0, 10); // "yyyy-MM-dd"
            person.setDob(LocalDate.parse(dobString));
        }

        Map<String, Object> address = (Map<String, Object>) personalData.get("address");
        if (address != null) {
            person.setFullAddress((String) address.get("full_address"));
            person.setCountry((String) address.get("country"));
            person.setState((String) address.get("state"));
            person.setCity((String) address.get("city"));
            person.setPincode((String) address.get("pincode"));
        }

        String rawPassword = (String) authData.get("password");
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        person.setPassword(passwordEncoder.encode(rawPassword));

        person.setOrganization(org);
        ValidationUtils.validateEmail(person.getEmail());

        return personRepository.save(person);
    }

    public Person createPerson(Person person) {
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

    public Person updatePerson(Long id, PersonDTO personDTO) {
        Person person = findById(id);

        if (personDTO.getName() != null) person.setName(personDTO.getName());
        if (personDTO.getEmail() != null) {
            ValidationUtils.validateEmail(personDTO.getEmail());
            person.setEmail(personDTO.getEmail());
        }
        if (personDTO.getMobile() != null) person.setMobile(personDTO.getMobile());
        if (personDTO.getGender() != null) person.setGender(personDTO.getGender());
        if (personDTO.getBloodGroup() != null) person.setBloodGroup(personDTO.getBloodGroup());

        return personRepository.save(person);
    }

    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return personRepository.existsByEmail(email);
    }

    public Person findByEmailOrUserId(String emailOrUserId) {
        return personRepository.findByEmail(emailOrUserId)
                .or(() -> personRepository.findByUserId(emailOrUserId))
                .orElseThrow(() -> new RuntimeException("Person not found"));
    }
}
