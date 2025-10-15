package com.medicconnect;

import com.medicconnect.models.Organization;
import com.medicconnect.models.Person;
import com.medicconnect.repositories.OrganizationRepository;
import com.medicconnect.repositories.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {  // or MedicConnectBackendApplication

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        System.out.println("MedicConnectBackend is running...");
    }

    // CommandLineRunner to test DB connection
    @Bean
    CommandLineRunner run(OrganizationRepository orgRepo, PersonRepository personRepo) {
        return args -> {
            // Create a sample organization
            Organization org = new Organization();
            org.setName("City Hospital");
            org.setCategory("Hospital");
            org.setRegistrationNumber("H2033");
            org.setYearOfEstablishment(2005);
            org.setOwnershipType("Private");
            orgRepo.save(org);

            // Create a sample person linked to the organization
            Person person = new Person();
            person.setName("John Doe");
            person.setDob("1985-06-15");
            person.setGender("Male");
            person.setBloodGroup("O+");
            person.setMobile("9876543210");
            person.setEmail("john.doe@example.com");
            person.setOrganization(org);
            personRepo.save(person);

            System.out.println("Sample data saved successfully!");
        };
    }
}
