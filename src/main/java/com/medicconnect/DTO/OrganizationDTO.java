package com.medicconnect.dto;

import com.medicconnect.models.Organization;

public class OrganizationDTO {

    private String organizationName;
    private String category;
    private String registrationNumber;
    private Integer yearOfEstablishment;
    private String ownershipType;
    private String email;
    private String mobile;
    private String type;       // Optional: type of org
    private String documents;  // JSON string of documents

    // ---------------- Getters & Setters ----------------
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

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDocuments() { return documents; }
    public void setDocuments(String documents) { this.documents = documents; }

    // ---------------- Convert DTO to Entity ----------------
    public Organization toOrganization() {
        Organization org = new Organization();
        org.setOrganizationName(this.organizationName);
        org.setCategory(this.category);
        org.setRegistrationNumber(this.registrationNumber);
        org.setYearOfEstablishment(this.yearOfEstablishment);
        org.setOwnershipType(this.ownershipType);
        org.setEmail(this.email);
        org.setMobile(this.mobile); // Match entity field
        org.setType(this.type != null ? this.type : "Unknown");
        org.setDocuments(this.documents);
        return org;
    }
}
