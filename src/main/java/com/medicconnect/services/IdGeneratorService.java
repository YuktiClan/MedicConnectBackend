package com.medicconnect.services;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdGeneratorService {

    private AtomicLong orgCounter = new AtomicLong(1000);
    private AtomicLong userCounter = new AtomicLong(10000);

    // Generate Org ID: e.g., HOSP1234, CLIN5678
    public String generateOrgId(String orgType) {
        long id = orgCounter.incrementAndGet();
        String prefix = orgType.equalsIgnoreCase("HOSPITAL") ? "HOSP" : "CLIN";
        return prefix + id;
    }

    // Generate User ID: e.g., ADMIN10001, DOC10002, PAT10003
    public String generateUserId(String role, String orgId) {
        long id = userCounter.incrementAndGet();
        String prefix = role.toUpperCase().substring(0, 3); // ADMIN -> ADM, DOCTOR -> DOC, PATIENT -> PAT
        return prefix + id;
    }

    // Generate Role ID for dashboard
    public String generateRoleId(String role) {
        long id = userCounter.incrementAndGet();
        return role.toUpperCase().substring(0, 3) + id;
    }
}
