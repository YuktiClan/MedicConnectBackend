package com.medicconnect.services;

import com.medicconnect.dto.OrganizationDTO;
import com.medicconnect.models.Organization;
import com.medicconnect.repositories.OrganizationRepository;
import com.medicconnect.utils.ValidationUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final IdGeneratorService idGeneratorService;

    public OrganizationService(OrganizationRepository organizationRepository,
                               IdGeneratorService idGeneratorService) {
        this.organizationRepository = organizationRepository;
        this.idGeneratorService = idGeneratorService;
    }
    
public Optional<Organization> findByOrganizationNameOptional(String name) {
    return organizationRepository.findByOrganizationName(name);
}


    @Transactional
    public Organization createOrganizationFromMap(Map<String, Object> orgData) {
        Organization org = new Organization();

        org.setOrganizationName((String) orgData.get("name"));
        org.setCategory((String) orgData.get("category"));
        org.setRegistrationNumber((String) orgData.get("registration_number"));
        org.setYearOfEstablishment(orgData.get("year_of_establishment") != null
                ? Integer.parseInt(orgData.get("year_of_establishment").toString())
                : null);
        org.setOwnershipType((String) orgData.get("ownership_type"));

        Map<String, Object> address = (Map<String, Object>) orgData.get("address");
        if (address != null) {
            org.setFullAddress((String) address.get("full_address"));
            org.setCountry((String) address.get("country"));
            org.setState((String) address.get("state"));
            org.setCity((String) address.get("city"));
            org.setPincode((String) address.get("pincode"));
        }

        org.setEmail((String) orgData.get("email"));
        org.setMobile((String) orgData.get("mobile"));
        org.setDocuments((String) orgData.get("documents"));

        ValidationUtils.validateEmail(org.getEmail());
        org.setOrgId(idGeneratorService.generateOrgId());

        return organizationRepository.save(org);
    }

    public Organization createOrganization(Organization org) {
        ValidationUtils.validateEmail(org.getEmail());
        org.setOrgId(idGeneratorService.generateOrgId());
        return organizationRepository.save(org);
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization updateOrganization(Long id, OrganizationDTO orgDTO) {
        Organization org = getOrganizationById(id);

        if (orgDTO.getOrganizationName() != null) org.setOrganizationName(orgDTO.getOrganizationName());
        if (orgDTO.getCategory() != null) org.setCategory(orgDTO.getCategory());
        if (orgDTO.getRegistrationNumber() != null) org.setRegistrationNumber(orgDTO.getRegistrationNumber());
        if (orgDTO.getOwnershipType() != null) org.setOwnershipType(orgDTO.getOwnershipType());
        if (orgDTO.getYearOfEstablishment() != null) org.setYearOfEstablishment(orgDTO.getYearOfEstablishment());

        return organizationRepository.save(org);
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    public Organization findByOrganizationName(String name) {
        return organizationRepository.findByOrganizationName(name)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }
}
