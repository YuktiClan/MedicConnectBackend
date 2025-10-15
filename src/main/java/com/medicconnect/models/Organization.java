package com.medicconnect.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "org")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String category;
    private String registrationNumber;
    private Integer yearOfEstablishment;
    private String ownershipType;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Person> persons;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public Integer getYearOfEstablishment() { return yearOfEstablishment; }
    public void setYearOfEstablishment(Integer yearOfEstablishment) { this.yearOfEstablishment = yearOfEstablishment; }

    public String getOwnershipType() { return ownershipType; }
    public void setOwnershipType(String ownershipType) { this.ownershipType = ownershipType; }

    public List<Person> getPersons() { return persons; }
    public void setPersons(List<Person> persons) { this.persons = persons; }
}
