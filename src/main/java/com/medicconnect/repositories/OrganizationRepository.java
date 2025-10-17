package com.medicconnect.repositories;

import com.medicconnect.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findByOrgId(String orgId);
    Optional<Organization> findByOrganizationName(String organizationName);
}
