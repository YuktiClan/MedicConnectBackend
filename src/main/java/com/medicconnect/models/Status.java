package com.medicconnect.models;

import java.util.Arrays;
import java.util.List;

public enum Status {
    PRE_REGISTRATION, // Before clicking pre-filled registration link
    PENDING,          // Waiting for main admin approval
    APPROVED,         // Fully active, can log in
    REJECTED;         // Denied by admin

    // Ordered workflow for validations or transitions
    private static final List<Status> ORDERED_WORKFLOW = List.of(
            PRE_REGISTRATION,
            PENDING,
            APPROVED,
            REJECTED
    );

    // Check if this status comes before another status in workflow
    public boolean isBefore(Status other) {
        return ORDERED_WORKFLOW.indexOf(this) < ORDERED_WORKFLOW.indexOf(other);
    }

    // Check if this status comes after another status in workflow
    public boolean isAfter(Status other) {
        return ORDERED_WORKFLOW.indexOf(this) > ORDERED_WORKFLOW.indexOf(other);
    }

    // Utility to get next logical status (returns null if last)
    public Status next() {
        int idx = ORDERED_WORKFLOW.indexOf(this);
        if (idx < 0 || idx + 1 >= ORDERED_WORKFLOW.size()) return null;
        return ORDERED_WORKFLOW.get(idx + 1);
    }

    // Utility to get previous logical status (returns null if first)
    public Status previous() {
        int idx = ORDERED_WORKFLOW.indexOf(this);
        if (idx <= 0) return null;
        return ORDERED_WORKFLOW.get(idx - 1);
    }

    // Validate if a given string matches a Status enum
    public static boolean isValid(String status) {
        return Arrays.stream(values()).anyMatch(s -> s.name().equalsIgnoreCase(status));
    }
}
