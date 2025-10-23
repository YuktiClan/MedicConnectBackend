package com.medicconnect.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "document_id", unique = true, nullable = false, updatable = false)
    private String documentId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Lob
    @Column(name = "file_data", nullable = false, columnDefinition = "BLOB")
    private byte[] fileData;

    @Column(name = "verified", nullable = false)
    private Boolean verified = false;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    // ---------------- Relations (nullable for staged documents) ----------------
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    // ---------------- Lifecycle ----------------
    @PrePersist
    protected void onCreate() {
        if (this.documentId == null)
            this.documentId = "DOC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        if (this.uploadedAt == null)
            this.uploadedAt = LocalDateTime.now();
    }

    // ---------------- Getters & Setters ----------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }

    public Organization getOrganization() { return organization; }
    public void setOrganization(Organization organization) { this.organization = organization; }
}
