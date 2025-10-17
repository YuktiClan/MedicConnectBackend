package com.medicconnect.models;

import com.medicconnect.permissions.Permission;
import jakarta.persistence.*;
import java.time.LocalDate;       // for DOB
import java.time.LocalDateTime;   // for registrationDate, associatedDate

import java.util.List;
import java.util.UUID;



@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private String userId;

    @Column(nullable = false)
    private String name;

    private LocalDate dob;

    private String gender;
    private String bloodGroup;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String mobile;

    @Column(nullable = false)
    private String password;

    private String role;

    // -----------------------------
    // Address fields directly
    // -----------------------------
    private String fullAddress;
    private String country;
    private String state;
    private String city;
    private String pincode;

    // -----------------------------
    // Permissions
    // -----------------------------
    @ElementCollection(targetClass = Permission.class)
    @Enumerated(EnumType.STRING)
    private List<Permission> permissions;

    // -----------------------------
    // Timestamps
    // -----------------------------
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private LocalDateTime registrationDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime associatedDate;

    // -----------------------------
    // Relationships
    // -----------------------------
    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    // -----------------------------
    // Documents JSON
    // -----------------------------
    @Lob
    @Column(columnDefinition = "TEXT")
    private String documents;

    // -----------------------------
    // Lifecycle Hooks
    // -----------------------------
    @PrePersist
    protected void onCreate() {
        if (this.userId == null) this.userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (this.registrationDate == null) this.registrationDate = new Date();
    }

    // -----------------------------
    // Getters & Setters
    // -----------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

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

    public String getFullAddress() { return fullAddress; }
    public void setFullAddress(String fullAddress) { this.fullAddress = fullAddress; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public List<Permission> getPermissions() { return permissions; }
    public void setPermissions(List<Permission> permissions) { this.permissions = permissions; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getAssociatedDate() { return associatedDate; }
    public void setAssociatedDate(LocalDateTime associatedDate) { this.associatedDate = associatedDate; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }

    public String getDocuments() { return documents; }
    public void setDocuments(String documents) { this.documents = documents; }

}
