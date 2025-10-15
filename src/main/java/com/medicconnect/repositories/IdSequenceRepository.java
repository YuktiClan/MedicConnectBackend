package com.medicconnect.repositories;

import com.medicconnect.models.IdSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IdSequenceRepository extends JpaRepository<IdSequence, Long> {
    Optional<IdSequence> findByOrgIdAndRole(String orgId, String role);
}
