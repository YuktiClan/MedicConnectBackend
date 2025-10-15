package com.medicconnect.controllers;

import com.medicconnect.models.Organization;
import com.medicconnect.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationRepository organizationRepository;

    // Get all organizations
    @GetMapping
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    // Get organization by ID
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        return organizationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create a new organization
    @PostMapping
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationRepository.save(organization);
    }

    // Update an organization
    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long id, @RequestBody Organization updatedOrg) {
        return organizationRepository.findById(id).map(org -> {
            org.setName(updatedOrg.getName());
            org.setCategory(updatedOrg.getCategory());
            org.setRegistrationNumber(updatedOrg.getRegistrationNumber());
            org.setYearOfEstablishment(updatedOrg.getYearOfEstablishment());
            org.setOwnershipType(updatedOrg.getOwnershipType());
            organizationRepository.save(org);
            return ResponseEntity.ok(org);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Delete an organization
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        return organizationRepository.findById(id).map(org -> {
            organizationRepository.delete(org);
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
