package com.medicconnect.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegistrationRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Valid email is required")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "User type is required") // e.g., "Hospital", "Admin", etc.
    private String type;

    @NotBlank(message = "Password is required")
    private String password;

    // Optional fields for organization or personal info can be added here
    private String organizationName;
    private String category;

    public RegistrationRequest() {}

    public RegistrationRequest(String name, String email, String phone, String type, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.password = password;
    }

    // ----------------- Getters & Setters -----------------
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getOrganizationName() { return organizationName; }
    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
