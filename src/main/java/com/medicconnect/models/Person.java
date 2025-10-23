package com.medicconnect.models;

import com.medicconnect.permissions.Permission;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private String userId;

    // -----------------------------
    // Personal Information
    // -----------------------------
    @Column(name = "personal_name", nullable = false)
    private String name;

    @Column(name = "personal_dob")
    private LocalDate dob;

    @Column(name = "personal_gender")
    private String gender;

    @Column(name = "personal_blood_group")
    private String bloodGroup;

    // -----------------------------
    // Personal Contact
    // -----------------------------
    @Column(name = "personal_email", unique = true, nullable = false)
    private String email;

    @Column(name = "personal_mobile", unique = true, nullable = false)
    private String mobile;

    // -----------------------------
    // Auth
    // -----------------------------
    @Column(name = "auth_password", nullable = false)
    private String password;

    @Column(name = "auth_agreement")
    private Boolean agreement;

    // -----------------------------
    // Address
    // -----------------------------
    @Column(name = "personal_address_full_address")
    private String fullAddress;

    @Column(name = "personal_address_country")
    private String country;

    @Column(name = "personal_address_state")
    private String state;

    @Column(name = "personal_address_city")
    private String city;

    @Column(name = "personal_address_pincode")
    private String pincode;

    // -----------------------------
    // Documents
    // -----------------------------
    @Lob
    @Column(name = "personal_documents", columnDefinition = "TEXT")
    private String documents;

    // -----------------------------
    // Metadata
    // -----------------------------
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "associated_date")
    private LocalDateTime associatedDate;

    @ElementCollection(targetClass = Permission.class)
    @Enumerated(EnumType.STRING)
    private List<Permission> permissions;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    // -----------------------------
    // Lifecycle
    // -----------------------------
    @PrePersist
    protected void onCreate() {
        if (this.userId == null)
            this.userId = "USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (this.registrationDate == null)
            this.registrationDate = LocalDateTime.now();
        if (this.password != null && !this.password.startsWith("$2a$")) {
            this.password = new BCryptPasswordEncoder().encode(this.password);
        }
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

    public Boolean getAgreement() { return agreement; }
    public void setAgreement(Boolean agreement) { this.agreement = agreement; }

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

    public String getDocuments() { return documents; }
    public void setDocuments(String documents) { this.documents = documents; }

    public LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDateTime registrationDate) { this.registrationDate = registrationDate; }

    public LocalDateTime getAssociatedDate() { return associatedDate; }
    public void setAssociatedDate(LocalDateTime associatedDate) { this.associatedDate = associatedDate; }

    public List<Permission> getPermissions() { return permissions; }
    public void setPermissions(List<Permission> permissions) { this.permissions = permissions; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
}
