package com.medicconnect.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id", unique = true, nullable = false, updatable = false)
    private String orgId;

    // -----------------------------
    // Organization Details (Basic Info)
    // -----------------------------
    @Column(name = "organization_name")
    private String organizationName;

    @Column(name = "organization_category")
    private String category;

    @Column(name = "organization_registration_number")
    private String registrationNumber;

    @Column(name = "organization_year_of_establishment")
    private Integer yearOfEstablishment;

    @Column(name = "organization_ownership_type")
    private String ownershipType;

    // -----------------------------
    // Organization Contact Info
    // -----------------------------
    @Column(name = "organization_email", nullable = false)
    private String email;

    @Column(name = "organization_mobile")
    private String mobile;

    @Column(name = "organization_landline_country_code")
    private String landlineCountryCode;

    @Column(name = "organization_landline_area_code")
    private String landlineAreaCode;

    @Column(name = "organization_landline_local_number")
    private String landlineLocalNumber;

    // -----------------------------
    // Organization Address
    // -----------------------------
    @Column(name = "organization_address_full_address")
    private String fullAddress;

    @Column(name = "organization_address_country")
    private String country;

    @Column(name = "organization_address_state")
    private String state;

    @Column(name = "organization_address_city")
    private String city;

    @Column(name = "organization_address_pincode")
    private String pincode;

    // -----------------------------
    // Documents
    // -----------------------------
    @Lob
    @Column(name = "organization_documents", columnDefinition = "TEXT")
    private String documents;

    // -----------------------------
    // Metadata
    // -----------------------------
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Person> persons;

    // -----------------------------
    // Lifecycle
    // -----------------------------
    @PrePersist
    protected void onCreate() {
        if (this.orgId == null)
            this.orgId = "ORG" + String.format("%04d", (int) (Math.random() * 10000));
        if (this.createdAt == null)
            this.createdAt = new Date();
    }

    // -----------------------------
    // Getters & Setters
    // -----------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }

    public Integer getYearOfEstablishment() { return yearOfEstablishment; }
    public void setYearOfEstablishment(Integer yearOfEstablishment) { this.yearOfEstablishment = yearOfEstablishment; }

    public String getOwnershipType() { return ownershipType; }
    public void setOwnershipType(String ownershipType) { this.ownershipType = ownershipType; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getLandlineCountryCode() { return landlineCountryCode; }
    public void setLandlineCountryCode(String landlineCountryCode) { this.landlineCountryCode = landlineCountryCode; }

    public String getLandlineAreaCode() { return landlineAreaCode; }
    public void setLandlineAreaCode(String landlineAreaCode) { this.landlineAreaCode = landlineAreaCode; }

    public String getLandlineLocalNumber() { return landlineLocalNumber; }
    public void setLandlineLocalNumber(String landlineLocalNumber) { this.landlineLocalNumber = landlineLocalNumber; }

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

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public List<Person> getPersons() { return persons; }
    public void setPersons(List<Person> persons) { this.persons = persons; }
}
