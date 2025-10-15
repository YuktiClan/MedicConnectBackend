package com.medicconnect.services;

import com.medicconnect.models.Organization;
import com.medicconnect.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    // Create organization with generated orgId
    public Organization createOrganization(Organization org) {
        String orgId = idGeneratorService.generateOrgId(org.getType());
        org.setOrgId(orgId);
        return organizationRepository.save(org);
    }
}
