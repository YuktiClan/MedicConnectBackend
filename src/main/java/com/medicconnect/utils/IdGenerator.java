package com.medicconnect.utils;

import com.medicconnect.models.IdSequence;
import com.medicconnect.repositories.IdSequenceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IdGenerator {

    private final IdSequenceRepository sequenceRepository;

    public IdGenerator(IdSequenceRepository sequenceRepository) {
        this.sequenceRepository = sequenceRepository;
    }

    private String toBase36(long number) {
        return Long.toString(number, 36).toUpperCase();
    }

    public synchronized String generateOrgId(String type, long seqNumber) {
        String prefix = switch (type.toUpperCase()) {
            case "HOSPITAL" -> "H";
            case "CLINIC" -> "C";
            default -> "O";
        };
        return prefix + toBase36(seqNumber);
    }

    @Transactional
    public synchronized String generateUserId(String orgId, String role, String personalEmail, String personalPhone) {
        if ((personalEmail == null || personalEmail.isBlank()) && (personalPhone == null || personalPhone.isBlank())) {
            return null;
        }

        String roleKey = role.toUpperCase();

        IdSequence seq = sequenceRepository.findByOrgIdAndRole(orgId, roleKey)
                .orElseGet(() -> {
                    IdSequence newSeq = new IdSequence();
                    newSeq.setOrgId(orgId);
                    newSeq.setRole(roleKey);
                    newSeq.setLastValue(0L);
                    return newSeq;
                });

        long nextValue = seq.getLastValue() + 1;
        seq.setLastValue(nextValue);
        sequenceRepository.save(seq);

        String prefix = switch (roleKey) {
            case "ADMIN" -> "A";
            case "DOCTOR" -> "D";
            case "PATIENT" -> "P";
            default -> "U";
        };

        return String.format("%s-%s-%s", prefix, orgId, toBase36(nextValue));
    }
}
