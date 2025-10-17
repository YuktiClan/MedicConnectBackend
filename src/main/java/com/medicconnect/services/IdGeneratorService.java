package com.medicconnect.services;

import com.medicconnect.models.IdSequence;
import com.medicconnect.repositories.IdSequenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class IdGeneratorService {

    private final IdSequenceRepository sequenceRepository;

    public IdGeneratorService(IdSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    // -----------------------------
    // Generate Organization ID
    // Example: ORG1001, ORG1002, ...
    // -----------------------------
    @Transactional
    public synchronized String generateOrgId() {
        final String type = "ORG";

        Optional<IdSequence> seqOpt = sequenceRepository.findByTypeAndRole(type, null);

        IdSequence seq = seqOpt.orElseGet(() -> {
            IdSequence s = new IdSequence();
            s.setType(type);
            s.setRole(null);
            s.setLastValue(0L);
            return s;
        });

        long nextValue = seq.getLastValue() + 1;
        seq.setLastValue(nextValue);
        sequenceRepository.save(seq);

        // Human-friendly ID
        return "ORG" + (1000 + nextValue);
    }

    // -----------------------------
    // Generate User/Person ID based on role
    // Examples: ADM1001, DOC1001, PAT1001, USR1001
    // -----------------------------
    @Transactional
    public synchronized String generateUserId(String role) {
        final String type = "USER";
        final String roleKey = (role != null) ? role.toUpperCase() : "USER";

        Optional<IdSequence> seqOpt = sequenceRepository.findByTypeAndRole(type, roleKey);

        IdSequence seq = seqOpt.orElseGet(() -> {
            IdSequence s = new IdSequence();
            s.setType(type);
            s.setRole(roleKey);
            s.setLastValue(0L);
            return s;
        });

        long nextValue = seq.getLastValue() + 1;
        seq.setLastValue(nextValue);
        sequenceRepository.save(seq);

        // Prefix based on role
        String prefix = switch (roleKey) {
            case "ADMIN" -> "ADM";
            case "DOCTOR" -> "DOC";
            case "PATIENT" -> "PAT";
            default -> "USR";
        };

        return prefix + (1000 + nextValue);
    }
}
