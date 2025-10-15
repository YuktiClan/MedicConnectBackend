package com.medicconnect.repositories;

import com.medicconnect.models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    // Optional custom finders (add as needed)
    Organization findByOrgId(String orgId);
}
