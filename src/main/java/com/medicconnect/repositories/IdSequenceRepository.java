package com.medicconnect.repositories;

import com.medicconnect.models.IdSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdSequenceRepository extends JpaRepository<IdSequence, Long> {

    /**
     * Find IdSequence by type and role.
     * Used for generating sequential IDs for users or organizations.
     *
     * @param type Type of entity ("ORG" or "USER")
     * @param role Role for users (ADMIN, DOCTOR, PATIENT), null for organizations
     * @return Optional containing IdSequence if exists
     */
    Optional<IdSequence> findByTypeAndRole(String type, String role);
}
