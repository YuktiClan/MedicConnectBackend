package com.medicconnect.models;

import jakarta.persistence.*;

@Entity
@Table(
    name = "id_sequences",
    uniqueConstraints = @UniqueConstraint(columnNames = {"org_id", "role"})
)
public class IdSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_id")
    private String orgId;

    @Column(name = "role")
    private String role;

    @Column(name = "last_value")
    private Long lastValue;

    // -----------------------------
    // Getters and Setters
    // -----------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Long getLastValue() { return lastValue; }
    public void setLastValue(Long lastValue) { this.lastValue = lastValue; }
}
