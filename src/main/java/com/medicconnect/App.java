package com.medicconnect;

import com.medicconnect.dto.OrganizationDTO;
import com.medicconnect.dto.PersonDTO;
import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.services.OrganizationService;
import com.medicconnect.services.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("MedicConnectBackend is running...");
    }

    @Bean
    CommandLineRunner run(OrganizationService orgService, PersonService personService, BCryptPasswordEncoder encoder) {
        return args -> {

            // ----------------------
            // Sample organization
            // ----------------------
            String orgName = "City Hospital";
            Optional<Organization> optionalOrg = orgService.findByOrganizationNameOptional(orgName);
            Organization org;

            if (optionalOrg.isPresent()) {
                org = optionalOrg.get();
                System.out.println("Organization already exists: " + orgName);
            } else {
                OrganizationDTO orgDTO = new OrganizationDTO();
                orgDTO.setOrganizationName(orgName);
                orgDTO.setCategory("Hospital");
                orgDTO.setRegistrationNumber("H2033");
                orgDTO.setYearOfEstablishment(2005);
                orgDTO.setOwnershipType("Private");
                orgDTO.setMobile("01122334455");
                orgDTO.setEmail("contact@cityhospital.com");
                orgDTO.setDocuments("{\"license\":\"12345\",\"certifications\":[\"ISO9001\",\"NABH\"]}");

                org = orgDTO.toOrganization();
                org = orgService.createOrganization(org);

                System.out.println("Created sample organization: " + orgName);
            }

            // ----------------------
            // Sample person
            // ----------------------
            String email = "john.doe@example.com";
            if (!personService.existsByEmail(email)) {
                PersonDTO personDTO = new PersonDTO();
                personDTO.setName("John Doe");
                personDTO.setDob(LocalDate.parse("1985-06-15"));
                personDTO.setGender("Male");
                personDTO.setBloodGroup("O+");
                personDTO.setMobile("9876543210");
                personDTO.setEmail(email);
                personDTO.setPassword("default123");

                // Convert DTO to Entity and ensure all required fields are set
                Person person = personDTO.toPerson();
                person.setEmail(personDTO.getEmail()); // explicitly ensure email is set
                person.setOrganization(org);
                person.setPassword(encoder.encode(personDTO.getPassword()));
                person.setDocuments("{\"aadhar\":\"123412341234\",\"pan\":\"ABCDE1234F\"}");

                personService.createPerson(person);

                System.out.println("Sample person saved successfully: " + email);
            } else {
                System.out.println("Sample person already exists. Skipping insert.");
            }
        };
    }
}
