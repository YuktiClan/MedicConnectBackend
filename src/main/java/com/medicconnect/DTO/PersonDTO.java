package com.medicconnect.dto;

import com.medicconnect.models.Person;
import java.time.LocalDate;       // for DOB
import java.time.LocalDateTime;   // for registrationDate, associatedDate

import java.util.List;

public class PersonDTO {

    private String name;
    private LocalDate dob;
    private String gender;
    private String bloodGroup;
    private String email;
    private String mobile;
    private String password;
    private String role;
    private List<String> organizationIds;
    private String documents;  // JSON string for documents like Aadhar, PAN etc.

    // ---------------- Getters & Setters ----------------
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<String> getOrganizationIds() { return organizationIds; }
    public void setOrganizationIds(List<String> organizationIds) { this.organizationIds = organizationIds; }

    public String getDocuments() { return documents; }
    public void setDocuments(String documents) { this.documents = documents; }

    // ---------------- Convert DTO to Entity ----------------
    public Person toPerson() {
        Person person = new Person();
        person.setName(this.name);
        person.setDob(this.dob);
        person.setGender(this.gender);
        person.setBloodGroup(this.bloodGroup);
        person.setEmail(this.email);
        person.setMobile(this.mobile);
        person.setPassword(this.password);
        person.setRole(this.role != null ? this.role : "USER");
        person.setDocuments(this.documents);
        return person;
    }
}
