package com.medicconnect.models;

import jakarta.persistence.*;

@Entity
@Table(name = "id_sequences", uniqueConstraints = @UniqueConstraint(columnNames = {"orgId", "role"}))
public class IdSequence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orgId;
    private String role;
    private Long lastValue;

    // Getters and Setters
}
